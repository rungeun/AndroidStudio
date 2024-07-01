package com.example.databasetest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Memo(var no:Long?, var content:String, var datetime: Long)
class SqliteHelper(context: Context, name:String, version:Int) : SQLiteOpenHelper(context, name, null, version){
    override fun onCreate(db: SQLiteDatabase?) { //호출한 파일이 생성되어 있지 않으면 파일 생성
        val create = "create table memo(`no` integer primary key, content text, datetime integer)"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { // 생성은 되어 있지만 버전이 다르다면 실행
        // 테이블에 변경사항이 있을 경우 호출됨
        // SqliteHelper()의 생성자를 호출할 때 기존 데이터베이스와 version을 비교해서 더 높으면 호출된다.
    }

    // 데이터 입력 함수
    fun insertMemo(memo: Memo){
        // DB 가져오기
        val wd = writableDatabase
        // memo를 입력타입으로 변환
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)
        // DB에 넣기
        wd.insert("memo", null, values)
        // DB 닫기
        wd.close()
    }
    // 데이터 조회함수
    fun selectMemo(): MutableList<Memo> {
        val list = mutableListOf<Memo>()
        val select = "SELECT 'no', content, datetime FROM memo"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        cursor.use {
            while (it.moveToNext()) {
                val no = it.getLong(it.getColumnIndexOrThrow("no"))
                val content = it.getString(it.getColumnIndexOrThrow("content"))
                val datetime = it.getLong(it.getColumnIndexOrThrow("datetime"))
                val memo = Memo(no, content, datetime)
                list.add(memo)
            }
        }
        rd.close()
        return list
    }
    // 데이터 수정 함수
    fun updateMemo(memo: Memo) {
        val wd = writableDatabase
        val values = ContentValues().apply {
            put("content", memo.content)
            put("datetime", memo.datetime)
        }

        wd.update("memo", values, "no = ?", arrayOf(memo.no.toString()))
        wd.close()
    }

    // 데이터 삭제 함수
    fun deleteMemo(memo:Memo){
        val delete = "DELETE FROM memo WHERE no = ${memo.no}"
        val wd = writableDatabase
        wd.execSQL(delete)
        wd.close()
    }
}



