package com.example.hinvipskotlin

import android.os.Environment
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.IOException
import java.util.ArrayList


/**XMLActivity
 *
 * Convert the gml file into two usable list of Pair<Float, Float> to be used by the MapActivity
 *
 *
 */


data class User(
    public val id : String,
    public var name : String
)

class XMLActivity {

    // Initialisation of all the variables
    var xmax: Float = Float.NaN
    var ymax: Float = Float.NaN
    var xmin: Float = Float.NaN
    var ymin: Float = Float.NaN
    var zroof: Float = Float.NaN
    var zfloor: Float = Float.NaN
    var xml: String = String()


    //val profileName=intent.getStringExtra("map")
    // Initialisation of the containers of the data
    private var bound = ArrayList<ArrayList<PointF>>()
    private var space = ArrayList<ArrayList<PointF>>()
    private var names = ArrayList<String>()
    private var namesOfMulti = ArrayList<String>()





    /**init
     *
     * Basic operations when the Activity is created.
     * Here the file is defined by default. The function will be changed to take any file requested by the user
     * onCreate it translate the given gmlFile into a String usable by the XMLPullParser
     * Then it call the function of the Parser
     *
     */

    init {

        // 1st map : KU-EngBuilding-IndoorGML-2019-02-12
        // 2nd map : 313-4F-3D-190612
        // 3rd map:CentralPlaza-Nontexture
        // 4th map : test1

         var mApp = SelectingMapActivity()
         var string = user.name


        Log.e("HIIIFISFFIr", string )


        val path =
            Environment.getExternalStorageDirectory().path + File.separator + "Love" + File.separator + string
        //Get the text file
        val file = File(path)
//Read text from file
        val text = StringBuilder()

        try {

            val br = file.bufferedReader()


            br.useLines { lines ->
                lines.forEach {
                    text.append(it)
                    text.append('\n')
                }
            }


        } catch (e: IOException) {
            Log.e("StringBuilder", "Input/Output Exception")
        }

        xml = text.toString()


        minmax(xml)
        parse(xml)


    }


    /**minmax
     *
     * Transform a String object with IndoorGML properties into two list of exploitable coordinates
     *
     * @param xml :String contains the IndoorGML File
     *
     */
    private fun minmax(xml: String) {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(xml.reader())
            var pos = false

            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                var tagname = parser.name

                when (parser.eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("pos", ignoreCase = true)) {
                        pos = true

                    }
                    XmlPullParser.END_TAG -> if (tagname.equals("pos", ignoreCase = true)) {
                        pos = false
                    }
                    XmlPullParser.TEXT -> if (parser.text.isNotEmpty() && pos == true) {
                        var parts = parser.text.split(" ")


                        var x = parts[0].toFloat()
                        var y = parts[1].toFloat()
                        var z = parts[2].toFloat()

                        if (xmax.equals(Float.NaN) or (x > xmax)) {
                            xmax = x
                        }

                        if (ymax.equals(Float.NaN) or (y > ymax)) {
                            ymax = y
                        }

                        if (xmin.equals(Float.NaN) or (x < xmin)) {
                            xmin = x
                        }


                        if (ymin.equals(Float.NaN) or (y < ymin)) {
                            ymin = y
                        }

                        if (zroof.equals(Float.NaN) or (y > ymax)) {
                            zroof = z
                        }


                        if (zfloor.equals(Float.NaN) or (z < zfloor)) {
                            zfloor = z
                        }

                    }

                }
                eventType = parser.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    /**parse
     *
     * Transform a String object with IndoorGML properties into two list of exploitable coordinates
     *
     * @param xml :String contains the IndoorGML File
     *
     */
    private fun parse(xml: String) {
        var floor = true
        var real = true // Define if it is a real boundary or an imaginary one
        var spaces = false
        var bounds = false
        var pos = false

        val zfloor = this.zfloor
        var singlelist = ArrayList<PointF>()



        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(xml.reader())

            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                var tagname = parser.name

                when (parser.eventType) {
                    /////////////////////////////////////// STARTTAG //////////////////////////////////////////////////

                    XmlPullParser.START_TAG -> if (tagname.equals("linearring", ignoreCase = true)) {
                        //Log.e("Starttag",tagname)

                        // create a new instance of employee
                        floor = true
                        real = true

                        singlelist = ArrayList()
                    } else if (tagname.equals("pos", ignoreCase = true)) {
                        pos = true
                        //Log.e("Starttag",tagname)

                    } else if (tagname.equals("cellspace", ignoreCase = true)) {
                        names.add(parser.getAttributeValue(null, "id"))
                        Log.e("names",parser.getAttributeValue(null, "id"))
                        //Log.e("Name", parser.getProperty("id").toString())


                    } else if (tagname.equals("MultiLayeredGraph", ignoreCase = true)){
                        namesOfMulti.add(parser.getAttributeValue(null, "id"))
                        //Log.e("namesOfMulti",parser.getAttributeValue(null, "id"))
                        //Log.e("Name", parser.getProperty("id").toString())
                        whileLoops@ while (eventType != XmlPullParser.END_DOCUMENT) {
                            var tagnames = parser.name
                            when (parser.eventType) {
                                /////////////////////////////////////// STARTTAG //////////////////////////////////////////////////
                                XmlPullParser.START_TAG -> if (tagnames.equals("linearring", ignoreCase = true)) {}
                                else if (tagnames.equals("pos", ignoreCase = true)) {}
                                else if (tagnames.equals("cellspace", ignoreCase = true)) {}
                                else if (tagnames.equals("Point", ignoreCase = true)) {}
                                else if (tagnames.equals("cellspacegeometry", ignoreCase = true)) {}
                                else if (tagnames.equals("multiLayeredGraph", ignoreCase = true)){}

                                ////////////////////////////////////////// ENDTAG //////////////////////////////////////////////
                                XmlPullParser.END_TAG -> if (tagnames.equals("linearring", ignoreCase = true)) {}
                                else if (tagnames.equals("pos", ignoreCase = true)) {}
                                else if (tagnames.equals("cellspace", ignoreCase = true)) {}
                                else if (tagnames.equals("cellspacegeometry", ignoreCase = true)) {}
                                else if (tagnames.equals("Point", ignoreCase = true)) {}
                                else if (tagnames.equals("multiLayeredGraph", ignoreCase = true)){
                                    break@whileLoops
                                }
                            }
                            eventType = parser.next()
                        }
                    }

                    else if (tagname.equals("cellspaceboundarygeometry", ignoreCase = true)) {
                        //Log.e("Starttag",tagname)
                        spaces = false
                        bounds = true
                        singlelist = ArrayList()
                    } else if (tagname.equals("cellspacegeometry", ignoreCase = true)) {
                        //Log.e("Starttag",tagname)

                        spaces = true
                        bounds = false
                        singlelist = ArrayList()
                    }


                    ////////////////////////////////////////// ENDTAG //////////////////////////////////////////////


                    XmlPullParser.END_TAG -> if (tagname.equals("linearring", ignoreCase = true)) {
                        //Log.e("Endtag",tagname)
                        if (floor && spaces) {
                            space.add(singlelist)
                        }
                        if (bounds && real) {
                            bound.add(singlelist)
                        }
                    } else if (tagname.equals("pos", ignoreCase = true)) {
                        pos = false
                        //Log.e("Endtag",tagname)

                    }
                    else if (tagname.equals("MultiLayeredGraph", ignoreCase = true)){
                    }

                    else if (tagname.equals("cellspaceboundarygeometry", ignoreCase = true)) {
                        //Log.e("Endtag",tagname)
                        //if (singlelist.isNotEmpty()){
                        // bound.add(singlelist)
                        //}
                        bounds = false
                    } else if (tagname.equals("cellspacegeometry", ignoreCase = true)) {
                        spaces = false
                        //space.add(singlelist)s
                        //Log.e("Endtagtag",tagname)


                    }


                    ////////////////////////////////////////////////// TEXT ////////////////////////////////////////////////////


                    XmlPullParser.TEXT -> if (parser.text.isNotEmpty() && pos == true) {

                        //Log.e("Position", parser.text)
                        var parts = parser.text.split(" ")


                        var x = parts[0].toFloat()
                        var y = parts[1].toFloat()
                        var z = parts[2].toFloat()

                        var coor = PointF(x, y)
                        //Log.e("coor", "X: " + x.toString() + " Y: " + y.toString() + " Z: " + z.toString())

                        if (z != zfloor) {
                            floor = false
                        }

                        if (z == this.zroof) {
                            real = false
                        }

                        if (!singlelist.contains(coor)) {
                            singlelist.add(coor)

                        }

                        // Setting up the max and min so we can have the total size of the map
                        //And set get everything bac    0k to their new
                    }
                }
                eventType = parser.next()
            }
            //Log.e("test", bound.toString())
            //Log.e("test", space.toString())

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getBound() = bound
    fun getSpace() = space
    fun getNames() = names
}