package com.company.model.danger;

import com.company.model.board.Board;
import com.company.model.board.Cell;
import com.company.model.figure.FigureColor;
import com.company.model.figure.direction.Distance;
import com.company.model.figure.direction.Offset;
import com.company.model.unit.Unit;

import java.util.ArrayList;
import java.util.List;

//Таблица клеток, находящихся под боем
public class Danger {
    private final static boolean ON = true;

    private final Board board;
    private List<Cell> checkList;

    public Danger(Board board) {
        this.board = board;
    }

    public DangerMatrix toMatrix(FigureColor aggressorColor) {
        boolean[][] array = toArray(aggressorColor);
        return new DangerMatrix(array, checkList);
    }

    private boolean[][] toArray(FigureColor aggressorColor) {
        boolean[][] out = new boolean[Board.SIZE][Board.SIZE];

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Cell cell = new Cell(j, i);
                Unit unit = board.get(cell);
                if (!unit.isNull() && unit.getColor() == aggressorColor) {
                    updateArray(out, cell);
                }
            }

        }

        return out;
    }

    private void updateArray(boolean[][] array, Cell cell) {
        Unit unit = board.get(cell);
        Distance distance = unit.getDistance();
        Offset[] offsets = unit.getOffsetsAttack();

        for (Offset o : offsets) {

            Cell check = cell;
            List<Cell> list = new ArrayList<>();
            list.add(cell);
            while (true) {

                check = check.sum(o);
                if (!board.isCorrect(check)) {
                    break;
                }

                array[check.row][check.column] = ON;
                list.add(check);

                Unit other = board.get(check);
                if (!other.isNull() || distance == Distance.ONE) {
                    if(!other.isNull() && other.isKing()) {
                        list.remove(check);
                        checkList = list;
                    }
                    break;
                }

            }
        }
    }

}
