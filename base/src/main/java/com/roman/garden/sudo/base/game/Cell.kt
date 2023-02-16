package com.roman.garden.sudo.base.game

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Cell constructor(var row: Int, var col: Int, var group: Int, var value: Int = 0) {

    var optional = mutableListOf<Int>()
    var valid: Boolean = false
    var preSet: Boolean = false

    @Throws(JSONException::class)
    fun fromJson(jsonObject: JSONObject) {
        val rowT = jsonObject.getInt("row")
        val colT = jsonObject.getInt("col")
        val groupT = jsonObject.getInt("group")
        val valueT = jsonObject.getInt("value")
        val preSetT = jsonObject.getBoolean("preSet")
        val validT = jsonObject.getBoolean("valid")
        val array = jsonObject.getJSONArray("optional")
        var tmp = mutableListOf<Int>()
        for (i in 0 until array.length()){
            tmp.add(array.getInt(i))
        }
        this.row = rowT
        this.col = colT
        this.group = groupT
        this.value = valueT
        this.preSet = preSetT
        this.valid = validT
        this.optional.clear()
        this.optional.addAll(tmp)
    }

    @Throws(JSONException::class)
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("row", this.row)
        json.put("col", this.col)
        json.put("group", this.group)
        json.put("valid", this.valid)
        json.put("preSet", this.preSet)
        json.put("value", this.value)
        val array = JSONArray()
        optional.forEach { it ->
            array.put(it)
        }
        json.put("optional", array)
        return json
    }

    override fun toString(): String {
        return "{ row:$row, col:$col, group:$group, value:$value, preSet:$preSet, valid:$valid }"
    }


}