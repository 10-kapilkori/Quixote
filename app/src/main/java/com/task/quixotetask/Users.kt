package com.task.quixotetask

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class Users(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "number")
    val number: String,
    @ColumnInfo(name = "password")
    val password: String
)

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "path")
    val imagePath: String,
    @ColumnInfo(name = "email")
    val email: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        title = parcel.readString()!!,
        description = parcel.readString()!!,
        imagePath = parcel.readString()!!,
        email = parcel.readString()!!
    ) {

    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(id)
        p0?.writeString(title)
        p0?.writeString(description)
        p0?.writeString(imagePath)
        p0?.writeString(email)
    }

    companion object CREATOR : Parcelable.Creator<Notes> {
        override fun createFromParcel(parcel: Parcel): Notes {
            return Notes(parcel)
        }

        override fun newArray(size: Int): Array<Notes?> {
            return arrayOfNulls(size)
        }
    }
}


















