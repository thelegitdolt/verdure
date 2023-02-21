package com.teamtaiga.verdure.Stuff.World;

import com.teamtaiga.verdure.Stuff.VerdureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class TetrisCarver {
    private final int nodesCount;
    private final List<int[]> Positions;
    private List<int[]> LastTetrisPiece;

    public TetrisCarver(int nodesCount) {
        this.nodesCount = nodesCount;
        Positions = new ArrayList<>();
        generateInitialTetris(Positions, new int[]{0, 0}, nodesCount);
        LastTetrisPiece = Positions;
    }

    private static void generateInitialTetris(List<int[]> positions, int[] initialPos, int toGen) {
        positions.add(initialPos);
        if (toGen <= 0) {
            for (int[] cords : VerdureUtil.Randomize(VerdureUtil.DIRECTION_NO_DIAGONALS)) {
                int[] newCord = VerdureUtil.transformCords(initialPos, cords);
                if (!positions.contains(newCord)) {
                    generateInitialTetris(positions, newCord, toGen - 1);
                }
            }
        }
    }

    public void Carve(int times) {
        for (int amogus = 0; amogus < times; amogus++) {
            List<int[]> checker;
            for (int[] kernel : VerdureUtil.Randomize(VerdureUtil.DIRECTION_NO_DIAGONALS)) {
                checker = LastTetrisPiece;
                boolean hasNotCarved = true;
                for (int i = 0; i < nodesCount; i++) {
                    if (hasNotCarved) {
                        checker.set(i, VerdureUtil.transformCords(checker.get(i), kernel));
                        if (!new HashSet<>(Positions).containsAll(checker)) {
                            for (int[] ints : checker) {
                                if (!Positions.contains(ints)) {
                                    Positions.add(ints);
                                }
                            }
                            hasNotCarved = false;
                            LastTetrisPiece = checker;
                        }
                    }
                }
            }
        }
    }

    private void Centralize() {
        int big = -8191;
        int small = 8191;
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
        big = -8191;
        small = 8191;
        for (int i = 0; i < Positions.size(); i++) {
            if (Positions.get(i)[1] > big) {
                big = Positions.get(i)[0];
                Z = i;
            }
            if (Positions.get(i)[1] < small) {
                small = Positions.get(i)[0];
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