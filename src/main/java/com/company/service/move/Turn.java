package com.company.service.move;

import com.company.model.board.Board;
import com.company.model.board.Cell;
import com.company.model.chess_exception.ChessException;
import com.company.model.command.Command;
import com.company.model.danger.Danger;
import com.company.model.piece.figure.Rank;
import com.company.model.piece.figure.Team;
import com.company.model.player.Player;
import com.company.model.piece.Piece;
import com.company.model.board.Way;

public abstract class Turn {
    protected final Board board;

    public Turn(Board board) {
        this.board = board;
    }

    public void execute(Command command, Player player, Danger danger) {
        Cell[] cells = commandToCells(player, command);

        Cell from = cells[0];
        Cell to = cells[1];

        Way way = new Way(from, to);
        verifyAvailablePosition(board, way);

        Piece piece = board.get(from);
        if (piece.isNull()) {
            String message = messageNoUnit(from);
            throw new ChessException(message);
        }

        if (piece.getTeam() != player.getTeam()) {
            String message = messageAlienUnit(from);
            throw new ChessException(message);
        }

        specialVerify(way, danger);
        action(way, player.getTeam());

    }

    protected abstract void action(Way way, Team team);


    protected Cell getCellKing(Team team) {
        return board.find(Rank.KING, team);
    }

    protected boolean isCheck(Danger danger) {
        Cell cellKing = getCellKing(danger.getTeam());
        return danger.isUnderAttack(cellKing);
    }


    private void verifyAvailablePosition(Board board, Way way) {
        if (!board.isCorrect(way.from) || !board.isCorrect(way.to)) {
            String message = "move is off the board";
            throw new ChessException(message);
        }
    }


    protected static int sign(int num) {
        if (num == 0) {
            return 0;
        }
        return num > 0 ? 1 : -1;
    }

    protected abstract Cell[] commandToCells(Player player, Command command);

    protected abstract void specialVerify(Way way, Danger danger);

    protected abstract String messageNoUnit(Cell cell);
    protected abstract String messageAlienUnit(Cell cell);

}
