package ch.bailu.aat.services.dem;


public class MultiCell4 extends MultiCell {
    /**
     *      a  b
     *      c  d
     */

    private short a, b, c, d;
    private int dzx, dzy;

    private final DemProvider demtile;
    private final int dim;
    private final int total_cellsize;


    private MultiCell4(final DemProvider dem) {
        demtile = dem;
        dim = dem.getDim().DIM;
        total_cellsize=Math.round(dem.getCellsize()*4f);
    }

    @Override
    public void set(int x) {
            _set(x);
        dzx=_delta_zx();
        dzy=_delta_zy();
    }


    private void _set(int x) {
            /**
             *       a b
             *       x d
             */ 
        int a,b,c,d;

        if (demtile.inverseLatitude()==true && demtile.inverseLongitude()==false) { // NE
            a=x-dim;    
            b=a+1;
            c=x;
            d=x+1;

        } else if (demtile.inverseLatitude()==false && demtile.inverseLongitude()==false) { // SE{
            a=x;    
            b=x+1;
            c=x+dim;
            d=c+1;

        } else if (demtile.inverseLatitude()==false && demtile.inverseLongitude()==true) { // SW{
            a=x-1;    
            b=x;
            d=x+dim;
            c=d-1;
            

        } else { // NW
            b=x-dim;
            a=b-1;
            c=x-1;
            d=x;
        }
        this.a=demtile.getElevation(a);
        this.b=demtile.getElevation(b);
        this.c=demtile.getElevation(c);
        this.d=demtile.getElevation(d);

    }
    

    @Override
    public int delta_zx() {
        return dzx;
    }

    @Override
    public int delta_zy() {
        return dzy;
    }

    private int _delta_zx() {
        final int sum = ((b + d) - (a + c)); 
        return  (sum * 100) / total_cellsize;
    }

    private int _delta_zy() {
        final int sum = ((c + d) - (b + a)); 
        return (sum * 100)  / total_cellsize;
    }
}
