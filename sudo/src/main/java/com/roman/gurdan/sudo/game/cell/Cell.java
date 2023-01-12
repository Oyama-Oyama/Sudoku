package com.roman.gurdan.sudo.game.cell;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cell {

    public int row; // 行
    public int col; //列
    public int group; // 所在子宫格
    public int value = 0; // 值
    public boolean preSet = true; // 是否是预置值
    public boolean valid = true; // 是否正确
    public List<Integer> optional = new ArrayList<>();

    public Cell() {
    }

    public Cell(int row, int col, int group, int value) {
        this.row = row;
        this.col = col;
        this.group = group;
        this.value = value;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }



    public boolean fromJson(JSONObject jsonObject) throws JSONException {
        try{
            this.row = jsonObject.getInt("row");
            this.col = jsonObject.getInt("col");
            this.group = jsonObject.getInt("group");
            this.value = jsonObject.getInt("value");
            this.preSet = jsonObject.getBoolean("preSet");
            this.valid = jsonObject.getBoolean("valid");
        } catch (Exception e){
            throw e;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{" +
                "row:" + row +
                ", col:" + col +
                ", group:" + group +
                ", value:" + value +
                ", preSet:" + preSet +
                ", valid:" + valid +
                '}';
    }

}