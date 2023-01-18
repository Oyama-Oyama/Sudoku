package com.roman.gurdan.sudo.game.factory;

import com.roman.gurdan.sudo.game.Difficulty;
import com.roman.gurdan.sudo.game.GameSize;
import com.roman.gurdan.sudo.game.action.AMirror;
import com.roman.gurdan.sudo.game.action.MirrorImpl;
import com.roman.gurdan.sudo.game.cell.Cell;
import com.roman.gurdan.sudo.util.LogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public abstract class IGameCreator {

    protected Difficulty _difficulty;
    protected Random random = new Random();
    protected Cell[][] data;
    protected List<Integer> _src = null;
    private Stack<Cell> solvedCells = new Stack<>();
//    private List<Cell> cells = new ArrayList<>();
//    private Map<Integer, Cell> map = new HashMap<>();
//    private Map<Integer, List<Integer>> hadTried = new HashMap<>();

    public IGameCreator(Difficulty difficulty) {
        this._difficulty = difficulty;
        this.data = new Cell[getGameSize().getValue()][getGameSize().getValue()];
    }

    public void destroy() {
        this.data = null;
        if (_src != null) {
            _src.clear();
            _src = null;
        }
    }

    public void printGame() {
        for (int i = 0; i < getGameSize().getValue(); i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < getGameSize().getValue(); j++) {
                builder.append(data[i][j].value);
                builder.append("  ");
            }
            LogUtil.e(builder.toString());
        }
    }

    /**
     * 新建游戏
     */
    public void createGame() {
        long start = System.currentTimeMillis();
        LogUtil.e("create start:" + start);
        this.initCells();
        this.solve();
        this.holeGameData();
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                data[i][j].optional.clear();
            }
        }
        long end = System.currentTimeMillis();
        LogUtil.e("create" + (end - start) + "ms");
    }

    /**
     * 游戏长度
     *
     * @return
     */
    public abstract GameSize getGameSize();

    public Difficulty getDifficulty() {
        return _difficulty;
    }

    /**
     * 初始化棋盘
     */
    protected void initCells() {
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                if (data[i][j] == null) {
                    int group = calGroup(i, j);
                    Cell cell = new Cell(i, j, group, 0);
                    data[i][j] = cell;
                }
            }
        }
    }

    protected abstract int calGroup(int row, int col);

//    private void clearBuildData() {
//        cells.clear();
//        map.clear();
//        hadTried.clear();
//    }
//
//    /**
//     * 创建数独
//     */
//    protected void buildGame() {
//        int count = (int) Math.pow(getGameSize().getValue(), 2);
//        int curCol = random.nextInt(getGameSize().getValue());
//        int gRow = 0;
//        try {
//            while (gRow < count) {
//                int realRow = gRow % getGameSize().getValue();
//                int value = gRow / getGameSize().getValue() + 1;
//                int maxCol = curCol + getGameSize().getValue();
//                cells.clear();
//                getAbleCells(realRow, value, curCol, maxCol);
//
//                if (cells.size() > 0) {
//                    int selectIndex = random.nextInt(cells.size());
//                    Cell cell = cells.get(selectIndex);
//                    cell.value = value;
//                    curCol = cell.col;
//                    map.put(gRow, cell);
//                } else {
//                    gRow = backtrack(gRow);
//                    if (gRow == -1) {
//                        /**
//                         * 为避免生成数独耗时太久，回溯次数过多，重新生成游戏；
//                         * 最短可回溯 3 个数字，及 3 * getGameSize().getValue()
//                         */
//                        throw new IllegalStateException("生成失败");
//
//                    }
//                }
//                gRow++;
//            }
//            clearBuildData();
//        } catch (Exception e) {
//            clearBuildData();
//            buildGame();
//        }
//    }
//
//    /**
//     * @param gRow 全局行数
//     * @return
//     */
//    protected int backtrack(int gRow) {
//        gRow -= 1;
//        int value = gRow / getGameSize().getValue() + 1;
//        cells.clear();
//        Cell preLineCell = map.get(gRow);
//
//        int preCol = preLineCell.col;
//        preLineCell.value = 0;
//        getAbleCells(preLineCell.row, value, 0, getGameSize().getValue());
//        Iterator<Cell> iterator = cells.iterator();
//        List<Integer> hasTriedCells = hadTried.get(gRow);
//        while (iterator.hasNext()) {
//            Cell next = iterator.next();
//            if (next.col == preCol) {
//                iterator.remove();
//                continue;
//            }
//            if (hasTriedCells != null && hasTriedCells.contains(next.col)) {
//                iterator.remove();
//                continue;
//            }
//        }
//        if (cells.size() > 0) {
//            int selectIndex = random.nextInt(cells.size());
//            Cell cell = cells.get(selectIndex);
//            cell.value = value;
//            map.put(gRow, cell);
//            if (hasTriedCells == null)
//                hasTriedCells = new ArrayList<>();
//            hasTriedCells.add(cell.col);
//            hadTried.put(gRow, hasTriedCells);
//            return gRow;
//        } else {
//            List<Integer> list = hadTried.remove(gRow);
//            if (list != null) list.clear();
//            return backtrack(gRow);
//        }
//    }
//
//    /**
//     * @param row    指定的棋盘 行
//     * @param value  值
//     * @param curCol 起始列
//     * @param maxCol 最终列
//     */
//    protected void getAbleCells(int row, int value, int curCol, int maxCol) {
//        for (int i = curCol; i < maxCol; i++) {
//            int realCol = i % getGameSize().getValue(); // 实际 列
//            Cell cell = data[row][realCol];
//            if (cell.value != 0) continue;
//            Set<Integer> src = new HashSet<>(getSrc());
//            src.removeAll(getUsedValues(cell));
//            if (src.contains(value)) {
//                cells.add(cell);
//            }
//        }
//    }

    /**
     * 数独解题
     *
     * @return
     */
    protected boolean solve() {
        solvedCells.clear();
        do {
            Cell cell = findHoleCell();
            if (cell == null) return true;
            cell.optional.clear();
            List<Integer> src = new ArrayList<>(getSrc());
            src.removeAll(getUsedValues(cell));
            if (src.size() == 0) {
                /**
                 *  回溯到上一个单元格，改变值
                 */
                try {
                    if (solvedCells.size() == 0) {
                        continue;
                    }
                    peekCell();
                } catch (Exception e) {
                    return false;
                }
            } else {
                cell.optional.addAll(src);
                src.clear();
                int index = random.nextInt(cell.optional.size());
                cell.value = cell.optional.remove(index);
                solvedCells.push(cell);
            }
        } while (true);
    }

    protected void peekCell() throws Exception {
        Cell cell;
        do {
            cell = solvedCells.pop();
            if (cell.optional.size() <= 0) {
                cell.value = 0;
            } else {
                int index = random.nextInt(cell.optional.size());
                cell.value = cell.optional.remove(index);
                solvedCells.push(cell);
                return;
            }
        } while (true);
    }

    /**
     * 从左上角开始找到一个空白单元格
     *
     * @return
     */
    protected Cell findHoleCell() {
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                if (data[i][j].value == 0)
                    return data[i][j];
            }
        }
        return null;
    }

    /**
     * 挖孔单元格
     */
    protected void holeGameData() {
        int count = getHoleCells();
        while (count > 0) {
            int x = random.nextInt(getGameSize().getValue());
            int y = random.nextInt(getGameSize().getValue());
            if (data[x][y].preSet) {
                data[x][y].preSet = false;
                data[x][y].value = 0;
                data[x][y].valid = false;
                count--;
            }
        }
    }

    /**
     * 所有数据
     *
     * @return
     */
    public Cell[][] getData() {
        return data;
    }

    /**
     * 基础数据
     *
     * @return
     */
    public List<Integer> getSrc() {
        if (_src == null) {
            _src = new ArrayList<>();
            for (int i = 0; i < getGameSize().getValue(); i++) {
                _src.add(i + 1);
            }
        }
        return _src;
    }

    /**
     * 复制单元格
     *
     * @param value
     * @param row
     * @param col
     * @param groupMask
     * @return
     */
    protected Cell copyCell(int value, int row, int col, int groupMask) {
        return new Cell(row, col, groupMask, value);
    }

    /**
     * 获取相关单元格内已使用的值
     *
     * @param cell
     * @return
     */
    public Set<Integer> getUsedValues(Cell cell) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                if (data[i][j] != null && data[i][j].value != 0) {
                    if (i == cell.row || j == cell.col || cell.group == data[i][j].group) {
                        set.add(data[i][j].value);
                    }
                }
            }
        }
        return set;
    }

    /**
     * 根据困难度 随机产生空白单元格数量
     *
     * @return
     */
    public int getHoleCells() {
        if (_difficulty == Difficulty.EASY) {
            return (int) Math.round(Math.pow(getGameSize().getValue(), 2) * getRate(0.35f, 0.2f));
        } else if (_difficulty == Difficulty.MEDIUM) {
            return (int) Math.round(Math.pow(getGameSize().getValue(), 2) * getRate(0.55f, 0.3f));
        } else if (_difficulty == Difficulty.HARD) {
            return (int) Math.round(Math.pow(getGameSize().getValue(), 2) * getRate(0.62f, 0.5f));
        }
        return getGameSize().getValue() / 2;
    }

    protected float getRate(float max, float min) {
        return min + random.nextFloat() * (max - min);
    }

    /**
     * 检查判定此单元格状态
     *
     * @param cell
     * @return
     */
    protected void checkValid(Cell cell) {
        int row = cell.row;
        int col = cell.col;
        int group = cell.group;
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                if (i == row || j == col || group == data[i][j].group) {
                    if (i == row && j == col && group == data[i][j].group) {

                    } else {
                        if (data[i][j].value == cell.value) {
                            cell.valid = false;
                            return;
                        }
                    }
                }
            }
        }
        cell.valid = true;
        return;
    }

    /**
     * 重置相关单元格状态
     *
     * @param row
     * @param col
     * @param group
     */
    protected void checkRelatedValid(int row, int col, int group) {
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                if (i == row || j == col || group == data[i][j].group) {
                    checkValid(data[i][j]);
                }
            }
        }
    }

    /**
     * 相关单元格
     *
     * @param cell
     * @param highLightLineOrRow
     * @param highLightGroup
     * @param highLightSameNumber
     * @return
     */
    public List<Cell> getRelativeCells(Cell cell, boolean highLightLineOrRow, boolean highLightGroup, boolean highLightSameNumber) {
        if (cell == null) return null;
        List<Cell> cells = new ArrayList<>();
        boolean status = false;
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                status = false;
                if (highLightLineOrRow) {
                    if (i == cell.row || j == cell.col) status = true;
                }
                if (highLightGroup) {
                    if (cell.group == data[i][j].group) status = true;
                }
                if (highLightSameNumber) {
                    if (cell.value != 0 && cell.value == data[i][j].value) status = true;
                }
                if (status) cells.add(data[i][j]);
            }
        }
        return cells;
    }

    /**
     * 返回值用于判定师傅需要检测 game over
     *
     * @param row
     * @param col
     * @param value
     * @return
     */
    public boolean setValue(int row, int col, int value, boolean note) {
        return this.setValue(getCell(row, col), value, note);
    }

    /**
     * 返回值用于判定师傅需要检测 game over
     *
     * @param cell
     * @param value
     * @return
     */
    public boolean setValue(Cell cell, int value, boolean note) {
        if (cell == null)
            throw new NullPointerException("cell can't be null");
        if (cell.preSet)
            return false;
        if (note) {
            if (cell.optional.contains(value)) {
                int index = cell.optional.indexOf(value);
                cell.optional.remove(index);
            } else cell.optional.add(value);
            return true;
        }
        int preValue = cell.value;
        cell.value = value;
        if (value == 0) {
            //此时置空单元格， 应检测相关单元格状态
            if (preValue != 0) {
                checkRelatedValid(cell.row, cell.col, cell.group);
            }
            return false;
        }
        // 检测此单元格状态
        checkValid(cell);
        if (cell.valid) {
            for (int i = 0; i < getGameSize().getValue(); i++) {
                for (int j = 0; j < getGameSize().getValue(); j++) {
                    if (i == cell.row || j == cell.col || cell.group == data[i][j].group) {
                        checkValid(data[i][j]);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 获取指定坐标单元格
     *
     * @param row
     * @param col
     * @return
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row > getGameSize().getValue())
            throw new IllegalArgumentException("row value invalid");
        if (col < 0 || col > getGameSize().getValue())
            throw new IllegalArgumentException("row value invalid");
        return data[row][col];
    }

    /**
     * 指定坐标单元格是否有效；值为0 亦为 无效
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isValid(int row, int col) {
        return this.isValid(getCell(row, col));
    }

    /**
     * 指定单元格是否有效； 值为0 亦为 无效
     *
     * @param cell
     * @return
     */
    public boolean isValid(Cell cell) {
        if (cell == null)
            throw new NullPointerException("cell can't be null");
        if (cell.preSet) return true;
        return cell.valid && cell.value != 0;
    }

    public AMirror recordMirrorImage() {
        MirrorImpl impl = new MirrorImpl(encodeData());
        return impl;
    }

    public boolean recoverMirrorImage(AMirror mirror) {
        if (mirror != null && mirror.valid())
            return decodeData(mirror.data);
        return false;
    }

    private String encodeData() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                Cell cell = data[i][j];
                builder.append(cell.value);
                builder.append("@");
                for (Integer integer : cell.optional) {
                    builder.append(String.valueOf(integer));
                }
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(";");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private boolean decodeData(String str) {
        try {
            String[] args = str.split(";");
            if (args.length == getGameSize().getValue()) {
                for (int i = 0; i < args.length; i++) {
                    String[] tmp = args[i].split(",");
                    if (tmp.length != getGameSize().getValue())
                        throw new IllegalStateException("invalid mirror");
                }
                for (int i = 0; i < args.length; i++) {
                    String[] tmp = args[i].split(",");
                    for (int j = 0; j < tmp.length; j++) {
                        Cell cell = data[i][j];
                        String[] content = tmp[j].split("@");
                        cell.value = Integer.parseInt(content[0]);
                        cell.optional.clear();
                        if (content.length > 1 && !content[1].isEmpty()) {
                            String[] ops = content[1].split("");
                            for (int i1 = 0; i1 < ops.length; i1++) {
                                if (!ops[i1].isEmpty())
                                    cell.optional.add(Integer.parseInt(ops[i1]));
                            }
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 重置游戏
     */
    public void resetGame() {
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                if (!data[i][j].preSet) {
                    data[i][j].value = 0;
                    data[i][j].optional.clear();
                }
            }
        }
    }

    /**
     * 判定游戏成功
     *
     * @return
     */
    public boolean isGameOver() {
        for (int i = 0; i < getGameSize().getValue(); i++) {
            for (int j = 0; j < getGameSize().getValue(); j++) {
                if (!data[i][j].preSet) {
                    if (data[i][j].value != 0 && data[i][j].valid) {

                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
