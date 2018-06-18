package com.qs.log.dao

import java.sql.{Connection, PreparedStatement}

import com.qs.log.model.StatEntity
import com.qs.log.utils.MYSQLUtils

import scala.collection.mutable.ListBuffer

/**
  * 统计 top N dao
  */
object StatTopNDao {

  /**
    * 批量保存到数据库
    *
    * @param statEntityList
    * @return
    */
  def insertAccessTopNIntercec(statEntityList: ListBuffer[StatEntity]): Int = {

    var connection: Connection = null
    var preparedStatement: PreparedStatement = null

    try {
      //      `date` int(11) DEFAULT NULL,
      //      `interName` text,
      //      `times` bigint(20) NOT NULL
      connection = MYSQLUtils.getConnection
      connection.setAutoCommit(false) //关闭自动提交

      val sql: String = "insert into accessdaylog (date,interName,times) values (?,?,?)"
      preparedStatement = connection.prepareStatement(sql)

      for (entity <- statEntityList) {
        preparedStatement.setLong(1, entity.date) //下标从1开始
        preparedStatement.setString(2, entity.interName)
        preparedStatement.setLong(3, entity.times)

        preparedStatement.addBatch() //添加到批处理中
      }
      val lines = preparedStatement.executeBatch() //执行批处理

      connection.commit() //手动提交

      lines.length
    } catch {
      case e: Exception => e.printStackTrace()
        0
    }
  }

}