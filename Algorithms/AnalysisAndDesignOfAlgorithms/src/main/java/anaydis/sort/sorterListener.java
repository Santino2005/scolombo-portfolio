package anaydis.sort;

import anaydis.sort.gui.SorterListener;

public class sorterListener implements SorterListener {

    private int Comparaciones = 0;
    private int swap = 0;
    private int copy = 0;

    @Override
    public void box(int from, int to) {
        System.out.println("Cambiando " + from + "a " + to);
    }
    @Override
    public void copy(int from, int to, boolean copyToAux) {
        copy++;
        System.out.println("Copiando " + from + "a " + to);
    }
    @Override
    public void equals(int i, int j) {
        //Como no tengo idea si se hace de otra formae lo dejo asi por las dudas
        Comparaciones++;
    }

    @Override
    public void greater(int i, int j) {
        //Lo mismo que el equals
        Comparaciones++;
    }

    @Override
    public void swap(int i, int j) {
        //..., copie la forma que lo hice desde el abstract
        swap++;
    }

    public int getComparaciones() {
        return Comparaciones;
    }

    public int getSwap() {
        return swap;
    }
    public void reset(){
        Comparaciones = 0;
        swap = 0;
        copy = 0;
    }
}
