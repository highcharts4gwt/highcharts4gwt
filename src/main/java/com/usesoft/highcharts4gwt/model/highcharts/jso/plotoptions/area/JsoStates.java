
package com.usesoft.highcharts4gwt.model.highcharts.jso.plotoptions.area;

import com.google.gwt.core.client.JavaScriptObject;
import com.usesoft.highcharts4gwt.model.highcharts.api.plotoptions.area.States;
import com.usesoft.highcharts4gwt.model.highcharts.api.plotoptions.area.states.Hover;

public class JsoStates
    extends JavaScriptObject
    implements States
{


    protected JsoStates() {
    }

    public final native Hover hover()
        throws RuntimeException /*-{
        return this["hover"] = (this["hover"] || {});
    }-*/
    ;

    public final native JsoStates hover(Hover hover)
        throws RuntimeException /*-{
        this["hover"] = hover;
        return this;
    }-*/
    ;

}