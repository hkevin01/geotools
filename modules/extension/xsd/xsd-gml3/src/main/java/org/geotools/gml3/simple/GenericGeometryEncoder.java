/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *   (C) 2016, Open Source Geospatial Foundation (OSGeo). 
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml3.simple;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.CompoundRing;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.GeometryEncoder;
import org.geotools.gml3.GML;
import org.geotools.xml.Encoder;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Helper class that encodes the geometries within GeometryCollection
 * 
 * @author 
 */
public class GenericGeometryEncoder extends GeometryEncoder<Geometry> {

    Encoder encoder;
    String gmlPrefix;
    String gmlUri;

    public GenericGeometryEncoder(Encoder encoder) {
        this(encoder, "gml", GML.NAMESPACE);
    }

    /**
     *
     * @param encoder
     * @param gmlPrefix
     */
    public GenericGeometryEncoder(Encoder encoder, String gmlPrefix, String gmlUri) {
        super(encoder);
        this.encoder = encoder;
        this.gmlPrefix = gmlPrefix;
        this.gmlUri = gmlUri;
    }

    @Override
    public void encode(Geometry geometry, AttributesImpl atts, GMLWriter handler) throws Exception {
        
        if (geometry instanceof LineString) {
            LineStringEncoder lineString = new LineStringEncoder(encoder,
                LineStringEncoder.LINE_STRING);
            lineString.encode((LineString) geometry, atts, handler);
        } else if (geometry instanceof Point) {
            PointEncoder pt = new PointEncoder(encoder, gmlPrefix, gmlUri);
            pt.encode((Point) geometry, atts, handler);
        } else if (geometry instanceof Polygon) {
            PolygonEncoder polygon = new PolygonEncoder(encoder, gmlPrefix, gmlUri);
            polygon.encode((Polygon) geometry, atts, handler);
        } else if (geometry instanceof MultiLineString) {
            MultiLineStringEncoder multiLineString = new MultiLineStringEncoder(encoder, gmlPrefix, gmlUri, true);
            multiLineString.encode((MultiLineString) geometry, atts, handler);
        } else if (geometry instanceof MultiPoint) {
            MultiPointEncoder multiPoint = new MultiPointEncoder(encoder, gmlPrefix, gmlUri);
            multiPoint.encode((MultiPoint) geometry, atts, handler);
        } else if (geometry instanceof MultiPolygon) {
            MultiPolygonEncoder multiPolygon = new MultiPolygonEncoder(encoder, gmlPrefix, gmlUri);
            multiPolygon.encode((MultiPolygon) geometry, atts, handler);
        } else if (geometry instanceof LinearRing) {
            LinearRingEncoder linearRing = new LinearRingEncoder(encoder, gmlPrefix, gmlUri);
            linearRing.encode((LinearRing) geometry, atts, handler);
        } else if (geometry instanceof CircularString || geometry instanceof CompoundCurve
                        || geometry instanceof CircularRing || geometry instanceof CompoundRing) {
            CurveEncoder curve = new CurveEncoder(encoder, gmlPrefix, gmlUri);
            curve.encode((LineString) geometry, atts, handler);
        } else {
            throw new Exception("Unsupported geometry " + geometry.toString());
        }
    }
}
