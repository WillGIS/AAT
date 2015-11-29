package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.srtm.SrtmAccess;
import ch.bailu.aat.views.graph.ColorTable;

public class ElevationColorTile extends ElevationTile {

    public ElevationColorTile(String id, SelfOn self, MapTile t) {
        super(id, self, t);
    }

   

    @Override
    public void fillBitmap(int[] buffer, int[] toLaRaster, int[] toLoRaster, Span laSpan, Span loSpan, SrtmAccess srtm) {
        int c=0;
        for (int la=laSpan.start; la< laSpan.end; la++) {
            for (int lo=loSpan.start; lo<loSpan.end; lo++) {
                final short altitude1 = srtm.getElevation(toLaRaster[la] * Srtmgl3TileObject.SRTM_BUFFER_DIM + toLoRaster[lo]);
                buffer[c]=ColorTable.altitude.getColor(altitude1);
                c++;
            }
        }
    }
    
    
    
    public static class Factory extends ObjectHandle.Factory {
        private final MapTile mapTile;

        public Factory(MapTile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            return  new ElevationColorTile(id, self, mapTile);
        }
    }

}