package ch.bailu.aat.services.cache;

import android.content.Context;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.dem.MultiCell;

public class HillshadeColorTable extends ObjectHandle {

    public final static String ID=HillshadeColorTable.class.getSimpleName();
    
    private final static int TABLE_DIM=500;
    private final static int TABLE_HDIM=TABLE_DIM/2;
    private final static int TABLE_SIZE=TABLE_DIM*TABLE_DIM;
    
    private final static int MIN_DELTA=-250;
    private final static int MAX_DELTA=240;

    private static final int COLOR=50;
    private static final int GRAY=(COLOR << 16) | (COLOR << 8) | COLOR;
    
    private byte table[][]=new byte[TABLE_DIM][TABLE_DIM];
    
    public HillshadeColorTable() {
        super(ID);
    }

    
    @Override
    public void onInsert(ServiceContext sc) {
        sc.getBackgroundService().process(new TableInitializer());
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {

    }

    @Override
    public void onChanged(String id, ServiceContext sc) {

    }

    
    public long getSize() {
        return TABLE_SIZE;
    }
    
    
    private static float indexToDelta(int i) {
        return (i-TABLE_HDIM) / 100f;
    }
    
    private static int deltaToIndex(int d) {
        return (d+TABLE_HDIM);
    }
    
    
    private static int cutDelta(int d) {
        d=Math.max(MIN_DELTA, d);
        d=Math.min(MAX_DELTA, d);
        return d;
    }
    
    
    public int getColor(final MultiCell mcell) {
        final int x=deltaToIndex(cutDelta(mcell.delta_zx()));
        final int y=deltaToIndex(cutDelta(mcell.delta_zy()));
        final int alpha=table[x][y] & 0xFF;
        
        return (alpha << 24) | GRAY;
    }
    
    
    private class TableInitializer extends ProcessHandle {
        /**
         * Source: 
         * http://edndoc.esri.com/arcobjects/9.2/net/shared/geoprocessing/spatial_analyst_tools/how_hillshade_works.htm
         */
        
        private static final double ALTITUDE_DEG=45f;

        private static final double AZIMUTH_DEG=315f;
        private static final double AZIMUTH_MATH = 360f - AZIMUTH_DEG + 90f;
        private        final double AZIMUTH_RAD = Math.toRadians(AZIMUTH_MATH);
        
        private static final double ZENITH_DEG=(90d-ALTITUDE_DEG);
        private        final double ZENITH_RAD=Math.toRadians(ZENITH_DEG);
        private        final double ZENITH_COS=Math.cos(ZENITH_RAD);
        private        final double ZENITH_SIN=Math.sin(ZENITH_RAD);
        

        private final static double DOUBLE_PI=Math.PI*2d;
        private final static double HALF_PI=Math.PI/2d;
        private final static double ONEHALF_PI=DOUBLE_PI-HALF_PI;

        
        @Override
        public long bgOnProcess() {
            for (int x=0; x<TABLE_DIM; x++) {
                for (int y=0; y<TABLE_DIM; y++) {
                    table[x][y]=hillshade(indexToDelta(x), indexToDelta(y));
                }
            }
            
            return TABLE_SIZE;
        }
        
        
        public byte hillshade(float dzx, float dzy) {
            
            final double slope=slope_rad(dzx, dzy);
            
            int 
            shade = (int) (255d * (( ZENITH_COS * Math.cos(slope) ) + 
                    ( ZENITH_SIN * Math.sin(slope) * Math.cos(AZIMUTH_RAD - aspect_rad(dzx, dzy)) ) ));
            
            shade = 255-Math.max(0, shade); 
                    
            return (byte) shade;
        }
        
       
        private double slope_rad(final double dzx, final double dzy) {
            return Math.atan(Math.sqrt(dzx*dzx + dzy*dzy));
        }

        
        private double aspect_rad(final double dzx, final double dzy) {
            double ret=0f;
            
            if (dzx!=0) {
                ret = Math.atan2(dzy, -1d*dzx);

                if (ret < 0) {

                  ret = DOUBLE_PI + ret;
                }
                
            } else {
                if (dzy > 0) {
                    ret = HALF_PI;

                } else if (dzy < 0) {
                  ret = ONEHALF_PI;
                } 
            }
            return ret;
        }


        @Override
        public void broadcast(Context context) {
            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, ID);
        }
     }
    
    
    
    public static class Factory extends ObjectHandle.Factory {

        public Factory() {}

        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return  new HillshadeColorTable();
        }
    } 
}
