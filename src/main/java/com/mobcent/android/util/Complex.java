package com.mobcent.android.util;

public class Complex {
    public double image;
    public double real;

    public Complex() {
        this.real = 0.0d;
        this.image = 0.0d;
    }

    public Complex(double real, double image) {
        this.real = real;
        this.image = image;
    }

    public Complex(int real, int image) {
        this.real = (double) Integer.valueOf(real).floatValue();
        this.image = (double) Integer.valueOf(image).floatValue();
    }

    public Complex(double real) {
        this.real = real;
        this.image = 0.0d;
    }

    public Complex cc(Complex complex) {
        Complex tmpComplex = new Complex();
        tmpComplex.real = (this.real * complex.real) - (this.image * complex.image);
        tmpComplex.image = (this.real * complex.image) + (this.image * complex.real);
        return tmpComplex;
    }

    public Complex sum(Complex complex) {
        Complex tmpComplex = new Complex();
        tmpComplex.real = this.real + complex.real;
        tmpComplex.image = this.image + complex.image;
        return tmpComplex;
    }

    public Complex cut(Complex complex) {
        Complex tmpComplex = new Complex();
        tmpComplex.real = this.real - complex.real;
        tmpComplex.image = this.image - complex.image;
        return tmpComplex;
    }

    public int getIntValue() {
        return (int) Math.round(Math.sqrt((this.real * this.real) - (this.image * this.image)));
    }
}
