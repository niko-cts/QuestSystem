package net.playlegend.questsystem.database;

import net.playlegend.questsystem.QuestSystem;
import net.playlegend.questsystem.quest.steps.QuestStepType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton class to handle all database requests in one place.
 *
 * @author Niko
 */
public final class DatabaseHandler {

	private static final String ERROR_TABLE_EXISTS = "Table {0} does already exist!";
	private static final String ERROR_COULDNT_DELETE = "Could not perform delete.";
	private static final String ERROR_TABLE_DOESNT_EXIST = "The table {0} does not exist!";

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

		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.warning("Could not find mariaDB Driver! " + e.getMessage());
		}

		this.retrieveConfigData();
		if (this.username != null && !this.username.isEmpty()) {
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
		connectionString.append("jdbc:mariadb://").append(this.host).append(":").append(this.port).append("/");
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

	/**
	 * Creates a table in the database with the given columns.
	 *
	 * @param tableName        String - the name of the table.
	 * @param columns      List<String> - a list of the columns
	 */
	public void createTable(String tableName, List<String> columns) {
		if (!this.reconnectIfClosed())
			return;

		if (columns.isEmpty()) {
			logger.warning("Cannot create a table with empty columns: " + tableName);
			return;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("create table if not exists ").append(tableName).append("(");
		Iterator<String> column = columns.iterator();
		while (column.hasNext()) {
			sql.append(column.next());
			if (column.hasNext()) {
				sql.append(", ");
			}
		}
		sql.append(");");
		try (Statement stmt = this.dbConnection.createStatement()) {
			stmt.execute(sql.toString());
			logger.log(Level.INFO, "Created table: {0}", tableName);
		} catch (SQLException e) {
			logger.warning("Error while creation of table: " + e.getMessage());
		}
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

		try (Statement stmt = this.dbConnection.createStatement()) {
			return stmt.executeQuery(new StringBuilder().append("show tables like '")
					.append(tableName).append("';")
					.toString()).next();
		} catch (SQLException exception) {
			logger.log(Level.WARNING, "Could not check if table exists! {0}", exception.getMessage());
			return this.reconnectIfClosed() && doesTableExist(tableName);
		}
	}

	/**
	 * Inserts multiple values into multiple tables.
	 * A null value splits the statement and creates a new insertion.
	 * Values and stringtypes need to have the same size!
	 *
	 * @param tableNames  List<String> - tables to insert to.
	 * @param values      List<List<String>> - the list all values - 'null' will add another line to the insertion in the same table.
	 * @param stringTypes List<List<Boolean>> - the list for every value that marks it as a string (true) or other (false).
	 */
	public void insertIntoTable(List<String> tableNames, List<List<String>> values, List<List<Boolean>> stringTypes) {
		if (!this.reconnectIfClosed())
			return;

		try (Statement stmt = this.dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			for (int i = 0; i < tableNames.size(); i++) {
				StringBuilder sql = new StringBuilder().append("insert into ").append(tableNames.get(i)).append(" values(");
				for (int j = 0; j < values.get(i).size(); j++) {
					if (values.get(i).get(j) == null) {
						stmt.addBatch(sql.append(")").toString());
						sql = new StringBuilder().append("insert into ").append(tableNames.get(i)).append(" values(");
						continue;
					}

					if (stringTypes.get(i).get(j)) {
						sql.append("'").append(values.get(i).get(j)).append("'");
					} else {
						sql.append(values.get(i).get(j));
					}
					if (j != values.get(i).size() - 1 && values.get(i).get(j + 1) != null)
						sql.append(",");
				}
				stmt.addBatch(sql.append(")").toString());
			}
			stmt.executeBatch();
			logger.log(Level.INFO, "Inserted into table: {0}", tableNames);
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not insert into table {0}: {1}", new String[]{tableNames.toString(), e.getMessage()});
		}
	}

	/**
	 * Returns the result of the select or null if the select could not be executed.
	 *
	 * @param tableName   String - the table name.
	 * @param columns     List<String> - a list of columns to be selected.
	 * @param whereClause String - the where clause.
	 * @return ResultSet or null
	 */
	public ResultSet select(String tableName, List<String> columns, String whereClause) {
		if (!this.reconnectIfClosed())
			return null;

		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i));
			if (i < columns.size() - 1)
				sql.append(",");
		}

		sql.append(" from ").append(tableName).append(" ").append(whereClause).append(";");

		try (Statement stmt = this.dbConnection.createStatement()) {
			return stmt.executeQuery(sql.toString());
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not perform select on table {0}: {1} (code: {2})", new String[]{tableName, e.getMessage(), e.getErrorCode() + ""});
			logger.log(Level.WARNING, "SQL-Query: {0}", sql.toString());
			return null;
		}
	}

	/**
	 * Executes an SQL script from the plain String.
	 * @param sql String - the sql script.
	 * @return ResultSet - the query result
	 */
	public ResultSet executeSQL(String sql) {
		if (!this.reconnectIfClosed())
			return null;

		try (Statement stmt = this.dbConnection.createStatement()) {
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not perform select on table {0}: {1} (code: {2})", new String[]{tableName, e.getMessage(), e.getErrorCode() + ""});
			logger.log(Level.WARNING, "SQL-Query: {0}", sql);
			return null;
		}
	}


	/**
	 * Tries to update a batch of rows based on the given columns, values, dataTypes and the where clause.
	 *
	 * @param tableNames   List<String> - the table names.
	 * @param allColumns   List<List<String>> - a list of all list column names.
	 * @param allValues    List<List<String>> - a list of all list values.
	 * @param allDataTypes List<List<String>> - a list of all list dataTypes.
	 * @param whereClauses List<String> - the where clauses;
	 * @return True/False whether the query got executed.
	 */
	public boolean update(List<String> tableNames, List<List<String>> allColumns, List<List<String>> allValues, List<List<String>> allDataTypes, List<String> whereClauses) {
		if (!this.reconnectIfClosed()) {
			return false;
		}

		if (tableNames.size() != whereClauses.size() || allColumns.size() != allValues.size() || allColumns.size() != allDataTypes.size() || whereClauses.size() != allColumns.size()) {
			logger.warning("Length of columns is not equal to the length of tableNames, values, data types or where clause.");
			return false;
		}

		try (Statement stmt = this.dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
			for (int w = 0; w < whereClauses.size(); w++) {
				List<String> columns = allColumns.get(w);
				List<String> values = allValues.get(w);
				List<String> dataTypes = allDataTypes.get(w);

				if (columns.size() != values.size() || columns.size() != dataTypes.size()) {
					logger.warning("Length of columns is not equal to the length of values or length of data types.");
					return false;
				}

				stmt.addBatch(getUpdateBatchData(tableNames.get(w), columns, values, dataTypes, whereClauses.get(w)));
			}
			stmt.executeBatch();
			return true;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not execute update! {0}", e.getMessage());
			return false;
		}
	}

	/**
	 * Generates the update batch data.
	 *
	 * @param tableName   String - the table name
	 * @param columns     List<String> - all columns of the row
	 * @param values      List<String> - all values of the row
	 * @param dataTypes   List<String> - all data types of the row
	 * @param whereClause String - the where clause
	 * @return String - the finished statement batch.
	 */
	private String getUpdateBatchData(String tableName, List<String> columns, List<String> values, List<String> dataTypes, String whereClause) {
		StringBuilder sql = new StringBuilder().append("update ").append(tableName).append(" set ");
		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i)).append("=");
			if (dataTypes.get(i).equals("string"))
				sql.append("'").append(values.get(i)).append("'");
			else
				sql.append(values.get(i));
			if (i != columns.size() - 1) {
				sql.append(", ");
			}
		}
		return sql.append(" ").append(whereClause).toString();
	}

	/**
	 * Tries to delete rows with the given where clause.
	 *
	 * @param tableName   String - the table name.
	 * @param whereClause String - the where clause.
	 * @return True/False whether the query executed.
	 */
	public boolean delete(String tableName, String whereClause) {
		if (!this.reconnectIfClosed())
			return false;

		if (!this.doesTableExist(tableName)) {
			logger.log(Level.WARNING, ERROR_COULDNT_DELETE + " " + ERROR_TABLE_DOESNT_EXIST);
			return false;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(tableName).append(" ").append(whereClause).append(";");
		try (Statement stmt = this.dbConnection.createStatement()) {
			stmt.execute(sql.toString());
			return true;
		} catch (SQLException e) {
			logger.log(Level.WARNING, ERROR_COULDNT_DELETE + " {0}", e.getMessage());
			return false;
		}
	}


	/**
	 * Closes the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (!this.dbConnection.isClosed())
				this.dbConnection.close();
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Could not close the connection to the database! {0}", e.getMessage());
		}
	}
}
