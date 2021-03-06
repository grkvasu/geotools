package org.geotools.mbstyle.parse;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;

import org.geotools.mbstyle.BackgroundMBLayer;
import org.geotools.mbstyle.FillMBLayer;
import org.geotools.mbstyle.MBLayer;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.RasterMBLayer;
import org.geotools.mbstyle.SymbolMBLayer;
import org.geotools.mbstyle.SymbolMBLayer.Alignment;
import org.geotools.mbstyle.SymbolMBLayer.IconTextFit;
import org.geotools.mbstyle.SymbolMBLayer.Justification;
import org.geotools.mbstyle.SymbolMBLayer.SymbolPlacement;
import org.geotools.mbstyle.SymbolMBLayer.TextAnchor;
import org.geotools.mbstyle.SymbolMBLayer.TextTransform;
import org.geotools.mbstyle.SymbolMBLayer.TranslateAnchor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class StyleParseTests {

    /**
     * Verify that the root properties for a Mapbox Style are parsed correctly.
     */
    @Test
    public void testParseRootProperties() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("rootPropertyTest.json");
        MBStyle mapboxStyle = new MBStyle(jsonObject);

        assertEquals(29, mapboxStyle.getBearing().intValue());
        assertEquals(new Point2D.Double(-73.9749, 40.7736), mapboxStyle.getCenter());
        assertEquals("mapbox://fonts/test/{fontstack}/{range}.pbf", mapboxStyle.getGlyphs());
        assertEquals("rootPropertyTest", mapboxStyle.getName());
        assertEquals(50, mapboxStyle.getPitch().intValue());
        assertEquals("mapbox://sprites/test/ciym62qtf005d2rnopgu725qo", mapboxStyle.getSprite());
        assertEquals(12.5, mapboxStyle.getZoom().doubleValue(), .00001);
        assertNotNull(mapboxStyle.getMetadata());
    }

    /**
     * Verify that the default root properties for a Mapbox Style are provided correctly.
     */
    @Test
    public void testParseRootPropertyDefaults() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("rootPropertyTestDefaults.json");
        MBStyle mapboxStyle = new MBStyle(jsonObject);

        assertEquals(0, mapboxStyle.getBearing().intValue());
        assertNull(mapboxStyle.getCenter());
        assertNull(mapboxStyle.getGlyphs());
        assertNull(mapboxStyle.getName());
        assertEquals(0, mapboxStyle.getPitch().intValue());
        assertNull(mapboxStyle.getSprite());
        assertNull(mapboxStyle.getZoom());
    }

    private <T extends MBLayer> T getSingleLayerOfType(MBStyle s, Class<T> clazz) {
        assertEquals(1, s.layers().size());
        assertTrue(clazz.isInstance(s.layers().get(0)));
        return clazz.cast(s.layers().get(0));
    }

    /**
     * Verify that fill layer properties are parsed correctly.
     */
    @Test
    public void testParseFillLayer() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("fillStyleTest.json");
        MBStyle s = new MBStyle(jsonObject);
        FillMBLayer l = getSingleLayerOfType(s, FillMBLayer.class);
        assertEquals("fill", l.getType());
        assertEquals("testid", l.getId());
        assertEquals("geoserver-states", l.getSource());
        assertEquals(new Color(0xFF595E), l.getFillColor());
        assertEquals(0.84, l.getFillOpacity().doubleValue(), .0001);
        assertEquals(2, l.getFillTranslate().length);
        assertEquals(20.0, l.getFillTranslate()[0], .0001);
        assertEquals(20.0, l.getFillTranslate()[1], .0001);
        assertEquals(new Color(0x1982C4), l.getFillOutlineColor());
    }

    /**
     * Verify that raster layer properties are parsed correctly.
     */
    @Test
    public void testParseRasterLayer() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("rasterStyleTest.json");
        MBStyle s = new MBStyle(jsonObject);
        RasterMBLayer l = getSingleLayerOfType(s, RasterMBLayer.class);
        assertEquals("raster", l.getType());
        assertEquals("testid", l.getId());
        assertEquals("geoserver-raster", l.getSource());
        assertEquals(.59, l.getOpacity().doubleValue(), .00001);
        assertEquals(.69, l.getBrightnessMax().doubleValue(), .00001);
        assertEquals(.28, l.getBrightnessMin().doubleValue(), .00001);
        assertEquals(.94, l.getContrast().doubleValue(), .00001);
        assertEquals(250.0, l.getFadeDuration().doubleValue(), .00001);
        assertEquals(30.0, l.getHueRotate().doubleValue(), .00001);
        assertEquals(0.9, l.getSaturation().doubleValue(), .00001);
    }
    
    /**
     * Verify that symbol layer properties are parsed correctly.
     */
    @Test
    public void testParseSymbolLayer() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolStyleTest.json");
        MBStyle s = new MBStyle(jsonObject);
        SymbolMBLayer l = getSingleLayerOfType(s, SymbolMBLayer.class);
        assertEquals("symbol", l.getType());
        assertEquals("testid", l.getId());
        assertEquals("testsource", l.getSource());

        assertEquals(1.0, l.getTextLineHeight().doubleValue(), .00001);
        assertEquals(20.0, l.getTextSize().doubleValue(), .00001);
        assertEquals("homer-simpson", l.getIconImage());
        assertEquals(TextTransform.LOWERCASE, l.getTextTransform());
        assertEquals(40.0, l.getIconRotate().doubleValue(), .00001);
        assertEquals(Justification.LEFT, l.getTextJustify());
        // assertEquals( , l.getVisibility());
        assertEquals(0.1, l.getIconSize().doubleValue(), .00001);
        assertEquals(TextAnchor.TOP, l.getTextAnchor());
        assertEquals(Alignment.VIEWPORT, l.getTextPitchAlignment());
        assertEquals("{name}", l.getTextField());
        assertEquals(10.0, l.getTextRotate().doubleValue(), .00001);
        assertEquals(0.05, l.getTextLetterSpacing().doubleValue(), .00001);
        assertEquals(5.0, l.getTextMaxWidth().doubleValue(), .00001);
        assertEquals(Arrays.asList("Some Test Font", "Other Test Font"), l.getTextFont());
        assertEquals(new Color(0xFEDCBA), l.getTextColor());
        assertEquals(0.94, l.getTextOpacity().doubleValue(), .00001);
        assertEquals(new Color(0xABCDEF), l.getTextHaloColor());
        assertEquals(0.0, l.getTextHaloWidth().doubleValue(), .00001);
        assertEquals(0.0, l.getTextHaloBlur().doubleValue(), .00001);
        
        assertEquals(2, l.getIconOffset().length);
        assertEquals(-1, l.getIconOffset()[0], .00001);
        assertEquals(-10, l.getIconOffset()[1], .00001);
        
        assertEquals(2, l.getTextOffset().length);
        assertEquals(1, l.getTextOffset()[0], .00001);
        assertEquals(1, l.getTextOffset()[1], .00001);
        
        assertEquals(2, l.getIconTranslate().length);
        assertEquals(1, l.getIconTranslate()[0], .00001);
        assertEquals(1, l.getIconTranslate()[1], .00001);
        
        assertEquals(2, l.getTextTranslate().length);
        assertEquals(-1, l.getTextTranslate()[0], .00001);
        assertEquals(-1, l.getTextTranslate()[1], .00001);        
    }
    
    /**
     * Verify that symbol layer properties have correct defaults.
     */
    @Test
    public void testParseSymbolLayerDefaults() throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("symbolStyleTestDefaults.json");
        MBStyle s = new MBStyle(jsonObject);
        SymbolMBLayer l = getSingleLayerOfType(s, SymbolMBLayer.class);
        assertEquals("symbol", l.getType());
        assertEquals("testid", l.getId());
        assertEquals("testsource", l.getSource());

        assertEquals(SymbolPlacement.POINT, l.getSymbolPlacement());
        assertEquals(250.0 , l.getSymbolSpacing().doubleValue(), .00001);
        assertEquals(false, l.getSymbolAvoidEdges());
        assertEquals(false, l.getIconAllowOverlap());
        assertEquals(false, l.getIconIgnorePlacement());
        assertEquals(false, l.getIconOptional());
        assertEquals(Alignment.AUTO, l.getIconRotationAlignment());
        assertEquals(1.0, l.getIconSize().doubleValue(), .00001);
        assertEquals(IconTextFit.NONE, l.getIconTextFit());
        // assertEquals( l.getIconTextFitPadding());
        assertEquals(null, l.getIconImage());
        assertEquals(0.0, l.getIconRotate().doubleValue(), .00001);
        assertEquals(2.0, l.getIconPadding().doubleValue(), .00001);
        assertEquals(false, l.getIconKeepUpright());
        
        assertEquals(2, l.getIconOffset().length);
        assertEquals(0, l.getIconOffset()[0], .00001);
        assertEquals(0, l.getIconOffset()[1], .00001);
        
        assertEquals(Alignment.AUTO, l.getTextPitchAlignment());
        assertEquals(Alignment.AUTO, l.getTextRotationAlignment());
        assertEquals("", l.getTextField());
        assertEquals(Arrays.asList("Open Sans Regular","Arial Unicode MS Regular"), l.getTextFont());
        assertEquals(16.0, l.getTextSize().doubleValue(), .00001);
        assertEquals(10.0, l.getTextMaxWidth().doubleValue(), .00001);
        assertEquals(1.2, l.getTextLineHeight().doubleValue(), .00001);
        assertEquals(0.0, l.getTextLetterSpacing().doubleValue(), .00001);
        assertEquals(Justification.CENTER, l.getTextJustify());
        assertEquals(TextAnchor.CENTER, l.getTextAnchor());
        assertEquals(45.0, l.getTextMaxAngle().doubleValue(), .00001);
        assertEquals(2.0, l.getTextPadding().doubleValue(), .00001);
        assertEquals(true, l.getTextKeepUpright());
        assertEquals(TextTransform.NONE, l.getTextTransform());
        
        assertEquals(2, l.getTextOffset().length);
        assertEquals(0, l.getTextOffset()[0], .00001);
        assertEquals(0, l.getTextOffset()[1], .00001);
        
        assertEquals(false, l.getTextAllowOverlap());
        assertEquals(false, l.getTextIgnorePlacement());
        assertEquals(false, l.getTextOptional());
        assertEquals(1.0, l.getIconOpacity().doubleValue(), .00001);
        assertEquals(new Color(0x000000), l.getIconColor());
        assertEquals(new Color(0,0,0,0), l.getIconHaloColor());
        assertEquals(0, l.getIconHaloWidth().doubleValue(), .00001);
        assertEquals(0, l.getIconHaloBlur().doubleValue(), .00001);
        
        assertEquals(2, l.getIconTranslate().length);
        assertEquals(0, l.getIconTranslate()[0], .00001);
        assertEquals(0, l.getIconTranslate()[1], .00001);
        
        
        assertEquals(TranslateAnchor.MAP, l.getIconTranslateAnchor());
        assertEquals(1, l.getTextOpacity().doubleValue(), .00001);
        assertEquals(Color.black, l.getTextColor());
        assertEquals(new Color(0,0,0,0), l.getTextHaloColor());
        assertEquals(0.0, l.getTextHaloWidth().doubleValue(), .00001);
        assertEquals(0.0, l.getTextHaloBlur().doubleValue(), .00001);
        assertEquals(TranslateAnchor.MAP, l.getTextTranslateAnchor());
        
        assertEquals(2, l.getTextTranslate().length);
        assertEquals(0, l.getTextTranslate()[0], .00001);
        assertEquals(0, l.getTextTranslate()[1], .00001);    
    }
    

    /**
     * Verify that background layer properties are parsed correctly.
     */
    @Test
    public void testParseBackgroundLayer()  throws IOException, ParseException {
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("backgroundColorStyleTest.json");
        MBStyle s = new MBStyle(jsonObject);
        BackgroundMBLayer l = getSingleLayerOfType(s, BackgroundMBLayer.class);
        assertEquals(new Color(0x00FF00),  l.getBackgroundColor());
        assertEquals(0.45,  l.getBackgroundOpacity().doubleValue(), .00001);
        assertNull(l.getBackgroundPattern());
    }

}
