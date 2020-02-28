package online.kozubek.czoleczko.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import online.kozubek.czoleczko.Question
import online.kozubek.czoleczko.QuestionPackage

@Database(entities = [QuestionPackage::class, Question::class], version = 1)
@TypeConverters(QuestionTypeConverters::class)
abstract class QuestionDatabase : RoomDatabase(){
    abstract fun questionDao(): QuestionDao
    abstract fun questionPackageDao(): QuestionPackageDao
}