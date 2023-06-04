package edu.put.inf151312

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues

class MyDBHandler(context: Context,name: String?,
                  factory:SQLiteDatabase.CursorFactory?, version:Int): SQLiteOpenHelper(
    context,DATABASE_NAME,factory,DATABASE_VERSION){
    companion object{
        private val DATABASE_VERSION=1
        private val DATABASE_NAME="gamesDB.db"
        val TABLE_GAMES="games"
        val TABLE_EXPANSIONS="expansions"
        val COLUMN_ID="_id"
        val COLUMN_TITTLE="tittle"
        val COLUMN_YEAR="year"
        val COLUMN_IMAGE="image"
        val COLUMN_BGG_ID="bgg_id"
    }
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_GAMES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITTLE + " TEXT,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_BGG_ID + " LONG" +")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
        val CREATE_EXPANSIONS_TABLE = ("CREATE TABLE " +
                TABLE_EXPANSIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITTLE + " TEXT,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_BGG_ID + " LONG" +")")
        db.execSQL(CREATE_EXPANSIONS_TABLE)
    }


    override fun onUpgrade(db : SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        //db.execSQL( "DROP TABLE IF EXISTS " + TABLE_GAMES)
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_EXPANSIONS)
        onCreate(db)
    }

    fun addGame (game : Game) {
        val values = ContentValues()
        values.put (COLUMN_TITTLE , game.title)
        values.put (COLUMN_YEAR,game.year)
        values.put(COLUMN_IMAGE,game.image)
        values.put(COLUMN_BGG_ID,game.bgg_id)
        val db = this.writableDatabase
        db.insert(TABLE_GAMES,  null, values)
        db.close()
    }
    fun selectRow (rowN:Int):Game? {
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_ID = $rowN"
        val db = this.writableDatabase
        val cursor = db.rawQuery (query,null)
        var game: Game? = null

        if (cursor.moveToFirst ()){
            val id = (cursor.getInt (0))
            val title = cursor.getString (1)
            val year = cursor.getInt ( 2)
            val image = cursor.getString ( 3)
            val bgg_id = cursor.getLong ( 4)
            game = Game(id,title,year, image,bgg_id)
            cursor.close()
        }
        db.close()
        return game
    }
    fun clearData(db : SQLiteDatabase){

        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_GAMES)
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_EXPANSIONS)
        onCreate(db)
    }
    fun getRowCount(): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_GAMES"
        val cursor = db.rawQuery(query, null)
        var count = 0

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        return count
    }
    fun getAllGames(): MutableList<Game> {
        val gamesList: MutableList<Game> = mutableListOf()
        val query = "SELECT * FROM $TABLE_GAMES"

        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {

                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val year = cursor.getInt(2)
                val image = cursor.getString(3)
                val bggId = cursor.getLong(4)

                val game = Game(id, title, year, image, bggId)
                gamesList.add(game)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return gamesList
    }
    fun addExpansion (game : Game) {
        val values = ContentValues()
        values.put (COLUMN_TITTLE , game.title)
        values.put (COLUMN_YEAR,game.year)
        values.put(COLUMN_IMAGE,game.image)
        values.put(COLUMN_BGG_ID,game.bgg_id)
        val db = this.writableDatabase
        db.insert(TABLE_EXPANSIONS,  null, values)
        db.close()
    }
    fun selectRowExpansions (rowN:Int):Game? {
        val query = "SELECT * FROM $TABLE_EXPANSIONS WHERE $COLUMN_ID = $rowN"
        val db = this.writableDatabase
        val cursor = db.rawQuery (query,null)
        var game: Game? = null

        if (cursor.moveToFirst ()){
            val id = (cursor.getInt (0))
            val title = cursor.getString (1)
            val year = cursor.getInt ( 2)
            val image = cursor.getString ( 3)
            val bgg_id = cursor.getLong ( 4)
            game = Game(id,title,year, image,bgg_id)
            cursor.close()
        }
        db.close()
        return game
    }
    fun getRowCountExpansion(): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_EXPANSIONS"
        val cursor = db.rawQuery(query, null)
        var count = 0

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        return count
    }
    fun getAllExpansions(): MutableList<Game> {
        val gamesList: MutableList<Game> = mutableListOf()
        val query = "SELECT * FROM $TABLE_EXPANSIONS"

        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {

                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val year = cursor.getInt(2)
                val image = cursor.getString(3)
                val bggId = cursor.getLong(4)

                val game = Game(id, title, year, image, bggId)
                gamesList.add(game)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return gamesList
    }
    fun selectGameByID (bgg:Int?):Game? {
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_BGG_ID = $bgg"
        val db = this.writableDatabase
        val cursor = db.rawQuery (query,null)
        var game: Game? = null

        if (cursor.moveToFirst ()){
            val id = (cursor.getInt (0))
            val title = cursor.getString (1)
            val year = cursor.getInt ( 2)
            val image = cursor.getString ( 3)
            val bgg_id = cursor.getLong ( 4)
            game = Game(id,title,year, image,bgg_id)
            cursor.close()
        }
        db.close()
        return game
    }
    fun selectExpansionByID (bgg:Int?):Game? {
        val query = "SELECT * FROM $TABLE_EXPANSIONS WHERE $COLUMN_BGG_ID = $bgg"
        val db = this.writableDatabase
        val cursor = db.rawQuery (query,null)
        var game: Game? = null

        if (cursor.moveToFirst ()){
            val id = (cursor.getInt (0))
            val title = cursor.getString (1)
            val year = cursor.getInt ( 2)
            val image = cursor.getString ( 3)
            val bgg_id = cursor.getLong ( 4)
            game = Game(id,title,year, image,bgg_id)
            cursor.close()
        }
        db.close()
        return game
    }
}