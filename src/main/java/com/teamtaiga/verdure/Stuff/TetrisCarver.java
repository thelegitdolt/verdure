package com.teamtaiga.verdure.Stuff;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import java.util.List;

public class TetrisCarver {
    private final int nodesCount;
    private List<int[]> Positions;
    private List<int[]> LastTetrisPiece;
    private RandomSource Rand;


    public TetrisCarver(int nodesCount, RandomSource randomSource) {
        this.nodesCount = nodesCount;
        this.Rand = randomSource;
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
            int[] ect = VerdureUtil.DIRECTION_NO_DIAGONALS[Rand.nextInt(4)];
            List<int[]> checker;
            for (int[] kernel : VerdureUtil.Randomize(VerdureUtil.DIRECTION_NO_DIAGONALS)) {
                checker = LastTetrisPiece;
                boolean hasNotCarved = true;
                for (int i = 0; i < nodesCount; i++) {
                    if (hasNotCarved) {
                        checker.set(i, VerdureUtil.transformCords(checker.get(i), kernel));
                        if (!Positions.containsAll(checker)) {
                            for (int j = 0; j < checker.size(); j++) {
                                if (!Positions.contains(checker.get(j))) {
                                    Positions.add(checker.get(j));
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

    }

    private BlockPos[] ConvertToBlockPos(BlockPos origin) {
        return null;
    }
}