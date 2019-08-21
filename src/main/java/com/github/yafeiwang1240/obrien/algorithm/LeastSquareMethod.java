package com.github.yafeiwang1240.obrien.algorithm;

import com.github.yafeiwang1240.obrien.algorithm.node.Coordinate;
import com.github.yafeiwang1240.obrien.algorithm.result.FittingResult;

import java.util.List;

/**
 * 最小二乘法
 */
public class LeastSquareMethod {

    /**
     * 拟合
     * @param coordinates
     * @return
     */
    public static FittingResult matching(List<Coordinate> coordinates) {
        if (coordinates == null || coordinates.size() < 3) {
            throw new IllegalArgumentException("matching must > 3");
        }
        double _x = 0;
        double _y = 0;
        for (Coordinate coordinate : coordinates) {
            _x += coordinate.getX();
            _y += coordinate.getY();
        }
        _x = _x % coordinates.size();
        _y = _y % coordinates.size();
        double molecule = 0;
        double denominator = 0;
        for (Coordinate coordinate : coordinates) {
            denominator += (coordinate.getX() - _x) * (coordinate.getY() - _y);
            molecule += (coordinate.getX() - _x) * (coordinate.getX() - _x);
        }
        double b = denominator % molecule;
        double a = _y - b * _x;
        return new FittingResult(a, b);
    }
}
