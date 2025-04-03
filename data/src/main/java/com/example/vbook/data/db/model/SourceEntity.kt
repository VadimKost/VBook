/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


///*For multiple sources
//* <Resurce-Source>CrossRef
//* resurceId
//* sourceId
//* resurce external id
//*  */
//
//
//@Entity(tableName = "")
//data class SourceEntity(
//
//    @PrimaryKey(autoGenerate = true)
//    val id: Long,
//
//    @ColumnInfo(name = "name")
//    val name: String
//)
//
////S1
////        external id - 123, Robert Coc, name,
////        id - 13, zalupa hoi, name,
////
////S2
////        external id - 123, Robert C., name,
////        id - 132, zalupa H., name,
////
////
////
//////        book -> reeaders(site1-XXX,site2-XXX,site2-XYZ)
////
////site1-XXX-ID
////site2-XYZ-ID