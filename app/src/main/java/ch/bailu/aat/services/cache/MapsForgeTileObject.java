package ch.bailu.aat.services.cache;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.graphic.AppBitmap;

public class MapsForgeTileObject extends TileObject implements Observer {
    private final ServiceContext scontext;
    private final Tile tile;

    private TileBitmap bitmap = null;
    public MapsForgeTileObject(String id, ServiceContext sc, Tile t) {
        super(id);
        scontext = sc;
        tile = t;
        sc.getRenderService().lockToCache(this);
        retreiveBitmap();
    }

    private void retreiveBitmap() {
        TileBitmap b = scontext.getRenderService().getTile(tile);

        if (b != null) {
            bitmap = b;
            AppBroadcaster.broadcast(scontext.getContext(),
                    AppBroadcaster.FILE_CHANGED_INCACHE,
                    toString());
        }
    }

    @Override
    public Bitmap getBitmap() {
        if (bitmap != null) {
            return AndroidGraphicFactory.getBitmap(bitmap);
        }
        return null;
    }

    @Override
    public Drawable getDrawable(Resources res) {
        return null;
    }

    @Override
    public AppBitmap getAppBitmap() {
        return null;
    }

    @Override
    public void reDownload(ServiceContext sc) {

    }

    @Override
    public boolean isLoaded() {
        return getBitmap() != null;
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {

    }

    @Override
    public void onChanged(String id, ServiceContext sc) {

    }

    @Override
    public void onChange() {
        retreiveBitmap();
    }


    @Override
    public void onRemove(ServiceContext sc) {
        sc.getRenderService().freeFromCache(this);
        super.onRemove(sc);
    }


    @Override
    public long getSize() {
        return getBytesHack(tile.tileSize);
    }

    public Tile getTile() {
        return tile;
    }


    public static class Factory extends ObjectHandle.Factory {
        private final Tile mapTile;

        public Factory(Tile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return  new MapsForgeTileObject(id, sc, mapTile);
        }

    }


    public final static Source MAPSFORGE =
            new Source() {

                @Override
                public String getName() {
                    return "MapsForge*";
                }

                @Override
                public String getID(Tile t, Context x) {
                    return genID(t, MapsForgeTileObject.class.getSimpleName());
                }

                @Override
                public int getMinimumZoomLevel() {
                    return 0;
                }

                @Override
                public int getMaximumZoomLevel() {
                    return 17;
                }

                @Override
                public ObjectHandle.Factory getFactory(Tile mt) {
                    return  new Factory(mt);
                }

                @Override
                public TileBitmapFilter getBitmapFilter() {
                    return TileBitmapFilter.COPY_FILTER;
                }
            };
}