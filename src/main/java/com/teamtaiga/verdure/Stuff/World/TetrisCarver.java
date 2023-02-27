package com.teamtaiga.verdure.Stuff.World;

import com.teamtaiga.verdure.Stuff.VerdureUtil;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TetrisCarver {
    private final List<int[]> Positions;
    private final List<int[]> InitialTetris;

    public TetrisCarver(int nodesCount) {
        Positions = new ArrayList<>();
        InitialTetris = Positions;
        Positions.add(new int[]{0, 0});
        generateInitialTetris(new int[]{0, 0}, nodesCount);
    }

    private void generateInitialTetris(int[] initialPos, int toGen) {
        if (toGen > 1) {
            for (int[] cords : VerdureUtil.Randomize(VerdureUtil.DIRECTION_NO_DIAGONALS)) {
                int[] newCord = VerdureUtil.transformCords(initialPos, cords);
                if (!VerdureUtil.ArrayInList(Positions, newCord)) {
                    Positions.add(newCord.clone());
                    generateInitialTetris(newCord, toGen - 1);
                    break;
                }
            }
        }
    }

    public List<int[]> getPositions() {
        return Positions;
    }

    // Carve can only be used once!!!!
    public void Carve(int times) {
        Carve(this.InitialTetris, times);
    }

    private void Carve(List<int[]> piece, int times) {
        List<int[]> newPiece = new ArrayList<>();
        boolean check = true;
        if (times > 0) {
            for (int[] kernel : VerdureUtil.Randomize(VerdureUtil.DIRECTION_NO_DIAGONALS)) {
                if (check) {
                    for (int[] cord : piece) {
                        newPiece.add(VerdureUtil.transformCords(cord, kernel));
                    }
                    if (!VerdureUtil.ArrayAllInList(Positions, newPiece)) {
                        for (int[] newCord : newPiece) {
                            if (!VerdureUtil.ArrayInList(Positions, newCord)) {
                                Positions.add(newCord);
                            }
                        }
                        check = false;
                    }
                }
            }
            Carve(newPiece, times - 1);
        }
        else {
            Centralize();
        }
    }

    private void Centralize() {
        int big = -127;
        int small = 127;
        int indeX = 0;
        int smalldeX = 0;
        int Z = 0;
        int Zmol = 0;
        for (int i = 0; i < Positions.size(); i++) {
            if (Positions.get(i)[0] > big) {
                big = Positions.get(i)[0];
                indeX = i;
            }
            if (Positions.get(i)[0] < small) {
                small = Positions.get(i)[0];
                smalldeX = i;
            }
        }
        big = -127;
        small = 127;
        for (int i = 0; i < Positions.size(); i++) {
            if (Positions.get(i)[1] > big) {
                big = Positions.get(i)[1];
                Z = i;
            }
            if (Positions.get(i)[1] < small) {
                small = Positions.get(i)[1];
                Zmol = i;
            }
        }

        int XSurplus = ((Positions.get(indeX)[0] + Positions.get(smalldeX)[0])/2) ;
        int ZSurplus = ((Positions.get(Z)[1] + Positions.get(Zmol)[1])/2);
        Positions.forEach((n) -> {
            n[0] -= XSurplus;
            n[1] -= ZSurplus;
        });
    }

    public List<BlockPos> ConvertToBlockPos(BlockPos origin, int y) {
        List<BlockPos> list = new ArrayList<>();
        for (int[] offsets : Positions) {
            list.add(new BlockPos(origin.getX() + offsets[0], y, origin.getZ() + offsets[0]));
        }
        return list;
    }
}