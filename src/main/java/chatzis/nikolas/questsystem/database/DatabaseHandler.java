package chatzis.nikolas.questsystem.database;

import chatzis.nikolas.questsystem.QuestSystem;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A singleton class to handle all database requests in one place.
 *
 * @author Niko
 */
public final class DatabaseHandler {

	private static DatabaseHandler instance;

	/**
	 * Gets the instance of the DatabaseHandler.
	 *
	 * @return DatabaseHandler - the db handler.
	 * @see DatabaseHandler
	 * @since 0.0.1
	 */
	public static DatabaseHandler getInstance() {
		if (instance == null)
			instance = new DatabaseHandler();
		return instance;
	}

	private final FileConfiguration cfg;
	private final Logger logger;
	private String host;
	private String username;
	private String password;
	private String dbName;
	private int port;
	private Connection dbConnection;

	private DatabaseHandler() {
		this.cfg = QuestSystem.getInstance().getConfig();
		this.logger = QuestSystem.getInstance().getLogger();

		this.retrieveConfigData();
		if (this.username != null && !this.username.isBlank()) {
			this.connect();
		} else {
			logger.info("Database information was not set");
		}
	}

	private void retrieveConfigData() {
		this.host = this.cfg.getString("database.host");
		this.username = this.cfg.getString("database.user");
		this.dbName = this.cfg.getString("database.dbname");
		this.password = this.cfg.getString("database.pass");
		this.port = this.cfg.getInt("database.port");
	}


	/**
	 * Tries to connect to the database.
	 *
	 * @return True/False whether the plugin is connected to the db.
	 * @since 0.0.1
	 */
	private boolean connect() {
		StringBuilder connectionString = new StringBuilder();
		connectionString.append("jdbc:mysql://").append(this.host).append(":").append(this.port).append("/");
		connectionString.append(this.dbName).append("?user=").append(this.username).append("&password=").append(this.password).append("&useUnicode=true&passwordCharacterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&SslMode=Required");
		try {
			this.dbConnection = DriverManager.getConnection(connectionString.toString());
			logger.info("Connected to database!");
			return true;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not connect to database: {0}", e.getMessage());
			return false;
		}
	}

	/**
	 * Tries to reconnect if the connection was closed.
	 *
	 * @return True/False whether the plugin is connected to the db again.
	 * @since 0.0.1
	 */
	private boolean reconnectIfClosed() {
		try {
			return this.dbConnection != null && this.dbConnection.isValid(100) || this.connect();
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not reconnect to database: {0}", e.getMessage());
			return false;
		}
	}

	private boolean isInvalidSQLIdentifier(String identifier) {
		return identifier == null || !identifier.matches("[a-zA-Z0-9_]+");
	}

	/**
	 * Creates a table in the database with the given columns.
	 *
	 * @param tableName String - the name of the table.
	 * @param columns   List<String> - a list of the columns
	 */
	public boolean createTableIfNotExists(String tableName, List<String> columns) {
		if (!this.reconnectIfClosed())
			return false;

		if (columns.isEmpty()) {
			logger.warning("Could not create table! No given columns for table: {0}" + tableName);
			return false;
		}

		if (isInvalidSQLIdentifier(tableName)) {
			logger.log(Level.WARNING, "Could not create table! Invalid table name: {0}", tableName);
			return false;
		}

		StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");
		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i));
			if (i < columns.size() - 1) {
				sql.append(", ");
			}
		}
		sql.append(");");

		try (Statement stmt = this.dbConnection.createStatement()) {
			stmt.executeUpdate(sql.toString());
			logger.log(Level.INFO, "Table {0} created successfully.", tableName);
			return true;
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Could not create table {0}! {1}", new Object[]{tableName, exception.getMessage()});
		}
		return false;
	}

	/**
	 * Checks if a table with the given name already exists.
	 *
	 * @param tableName String - the name of the table.
	 * @return True/False
	 * @since 0.0.1
	 */
	public boolean doesTableExist(String tableName) {
		if (!this.reconnectIfClosed())
			return false;

		try {
			try (ResultSet rs = this.dbConnection.getMetaData().getTables(null, null, tableName, new String[]{"TABLE"})) {
				return rs.next();
			}
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Could not check if table exists! {0}", exception.getMessage());
			return this.reconnectIfClosed() && doesTableExist(tableName);
		}
	}

	/**
	 * Inserts multiple values into multiple tables.
	 * A null value splits the statement and creates a new insertion.
	 *
	 * @param tableNames List<String> - tables to insert to.
	 * @param values     List<List<Object>> - the list all values - 'null' will add another line to the insertion in the same table.
	 */
	public boolean insertIntoTable(List<String> tableNames, List<List<Object>> values) {
		if (!this.reconnectIfClosed())
			return false;

		StringBuilder preparedSQL = new StringBuilder();
		for (int i = 0; i < tableNames.size(); i++) {
			String tableName = tableNames.get(i);
			if (isInvalidSQLIdentifier(tableName)) {
				logger.log(Level.WARNING, "Could not insert into table! Invalid table name: {0}", tableName);
				return false;
			}
			preparedSQL.append("INSERT INTO ").append(tableName).append(" VALUES (");

			List<Object> valueList = values.get(i);

			for (int j = 0; j < valueList.size(); j++) {
				if (valueList.get(j) == null) {
					preparedSQL.append("),(");
					continue;
				}
				preparedSQL.append("?");
				if (j != valueList.size() - 1 && valueList.get(j + 1) != null) {
					preparedSQL.append(",");
				}
			}
			preparedSQL.append(");");
		}
		try (PreparedStatement pstmt = this.dbConnection.prepareStatement(preparedSQL.toString())) {
			// Set the parameters for the PreparedStatement
			int paramIndex = 1;
			for (List<Object> valuesOfTable : values) {
				for (Object value : valuesOfTable) {
					if (value != null) {
						pstmt.setObject(paramIndex++, value);
					}
				}
			}

			pstmt.execute();
			logger.log(Level.INFO, "Inserted into table: {0}", tableNames);
			return true;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not insert into table {0}: {1}", new Object[]{tableNames.toString(), e.getMessage()});
		}
		return false;
	}

	public Integer insertIntoTableAndGetGeneratedKey(String tableName, List<String> columns, List<Object> values) {
		if (!this.reconnectIfClosed())
			return null;

		if (isInvalidSQLIdentifier(tableName)) {
			logger.log(Level.WARNING, "Could not insert into table! Invalid table name: {0}", tableName);
			return null;
		}

		String sql = String.format(
				"INSERT INTO %s (%s) VALUES (%s)",
				tableName,
				String.join(",", columns),
				values.stream().map(v -> "?").collect(Collectors.joining(",")));
		try (var preparedStatement = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			for (int i = 0; i < values.size(); i++) {
				preparedStatement.setObject(i + 1, values.get(i));
			}
			preparedStatement.executeUpdate();
			var generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				logger.log(Level.SEVERE, "Could not insert into table {0}", tableName);
			}
		} catch (SQLException exception) {
			logger.log(Level.SEVERE, "Could not insert into table {0}: {1}", new String[]{tableName, exception.getMessage()});
		}
		return null;
	}

	/**
	 * Returns the result of the select or null if the select could not be executed.
	 *
	 * @param tableName   String - the table name.
	 * @param columns     List<String> - a list of columns to be selected.
	 * @param whereClause String - the where clause.
	 * @return ResultSet or null
	 */
	public ResultSet select(String tableName, List<String> columns, String whereClause, List<Object> parameters) {
		if (!this.reconnectIfClosed())
			return null;

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i));
			if (i < columns.size() - 1)
				sql.append(",");
		}

		sql.append(" FROM ").append(tableName).append(" ").append(whereClause).append(";");

		try (PreparedStatement pstmt = this.dbConnection.prepareStatement(sql.toString())) {
			for (int i = 0; i < parameters.size(); i++) {
				pstmt.setObject(i + 1, parameters.get(i));
			}

			return pstmt.executeQuery();
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not perform select on table {0}: {1} (code: {2})", new Object[]{tableName, e.getMessage(), e.getErrorCode()});
			logger.log(Level.WARNING, "SQL-Query: {0}", sql.toString());
			return null;
		}
	}

	/**
	 * Returns everything from that table.
	 *
	 * @param tableName String - the table name.
	 * @return ResultSet or null
	 */
	public ResultSet selectAll(String tableName) {
		if (isInvalidSQLIdentifier(tableName)) {
			logger.log(Level.WARNING, "Could not select table: {0}", tableName);
			return null;
		}
		try (PreparedStatement pstmt = this.dbConnection.prepareStatement(String.format("SELECT * FROM %s", tableName))) {
			return pstmt.executeQuery();
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not perform select on table {0}: {1} (code: {2})", new Object[]{tableName, e.getMessage(), e.getErrorCode()});
			return null;
		}
	}

	/**
	 * Executes an SQL script from the plain String.
	 *
	 * @param sql String - the sql script.
	 * @return boolean - operation was successful
	 */
	public boolean execute(String sql) {
		if (!this.reconnectIfClosed())
			return false;

		try (Statement statement = this.dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			statement.execute(sql);
			return true;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Could not perform sql:", e);
			logger.log(Level.SEVERE, "SQL-Query: {0}", sql);
			return false;
		}
	}


	/**
	 * Tries to update a batch of rows based on the given columns, values, dataTypes and the where clause.
	 *
	 * @param tableNames   List<String> - the table names.
	 * @param allColumns   List<List<String>> - a list of all list column names.
	 * @param allValues    List<List<Object>> - a list of all list values.
	 * @param whereClauses List<String> - the where clauses;
	 * @return True/False whether the query got executed.
	 */
	public boolean update(List<String> tableNames, List<List<String>> allColumns, List<List<Object>> allValues, List<String> whereClauses, List<List<Object>> whereObjects) {
		if (!this.reconnectIfClosed()) {
			return false;
		}

		if (tableNames.size() != whereClauses.size() || allColumns.size() != allValues.size() || whereClauses.size() != allColumns.size()) {
			logger.warning("Length of columns is not equal to the length of tableNames, values or where clause.");
			return false;
		}

		try {
			this.dbConnection.setAutoCommit(false);  // Begin transaction

			for (int w = 0; w < whereClauses.size(); w++) {
				List<String> columns = allColumns.get(w);
				List<Object> values = allValues.get(w);

				if (columns.size() != values.size()) {
					throw new SQLException("Length of columns is not equal to the length of values.");
				}

				String sql = getUpdateBatchData(tableNames.get(w), columns, whereClauses.get(w));
				try (PreparedStatement pstmt = this.dbConnection.prepareStatement(sql)) {
					int paramIndex = 1;
					for (Object value : values) {
						pstmt.setObject(paramIndex++, value);
					}
					for (Object whereObj : whereObjects.get(w)) {
						pstmt.setObject(paramIndex++, whereObj);
					}
					pstmt.addBatch();
				}
			}

			// Execute all batches
			try (Statement stmt = this.dbConnection.createStatement()) {
				stmt.executeBatch();
			}

			this.dbConnection.commit();  // Commit transaction
			logger.info("Updated tables: " + String.join(", ", tableNames));
			return true;
		} catch (SQLException e) {
			try {
				this.dbConnection.rollback();  // Rollback transaction on error
			} catch (SQLException rollbackEx) {
				logger.log(Level.SEVERE, "Rollback failed: {0}", rollbackEx.getMessage());
			}
			logger.log(Level.WARNING, "Could not execute update! {0}", e.getMessage());
			return false;
		} finally {
			try {
				this.dbConnection.setAutoCommit(true);  // Reset auto-commit to true
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, "Failed to reset auto-commit: {0}", ex.getMessage());
			}
		}
	}

	/**
	 * Generates the update batch data.
	 *
	 * @param tableName   String - the table name
	 * @param columns     List<String> - all columns of the row
	 * @param whereClause String - the where clause
	 * @return String - the finished statement batch.
	 */
	private String getUpdateBatchData(String tableName, List<String> columns, String whereClause) {
		StringBuilder sql = new StringBuilder().append("UPDATE ").append(tableName).append(" SET ");
		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i)).append(" = ?");
			if (i != columns.size() - 1) {
				sql.append(", ");
			}
		}
		sql.append(" ").append(whereClause);
		return sql.toString();
	}

	/**
	 * Tries to delete multiple rows throughout multiple tables.
	 *
	 * @param tableNames   List<String> - the table name.
	 * @param whereClauses List<String> - the where clauses.
	 */
	public boolean delete(List<String> tableNames, List<String> whereClauses) {
		if (!this.reconnectIfClosed())
			return false;

		if (tableNames.size() != whereClauses.size()) {
			logger.warning("Length of columns is not equal to the length of tableNames, values, data types or where clause.");
			return false;
		}

		try (Statement stmt = this.dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			for (int i = 0; i < tableNames.size(); i++) {
				String tableName = tableNames.get(i);
				String whereClause = whereClauses.get(i);
				stmt.addBatch(String.format("delete from %s %s;", tableName, whereClause));
			}
			stmt.executeBatch();
			logger.info("Deleted data in tables: " + String.join(", ", tableNames));
			return true;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not perform delete on {0}", e.getMessage());
			return false;
		}
	}


	/**
	 * Closes the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (this.dbConnection != null && !this.dbConnection.isClosed())
				this.dbConnection.close();
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not close the connection to the database! {0}", e.getMessage());
		}
	}
}
