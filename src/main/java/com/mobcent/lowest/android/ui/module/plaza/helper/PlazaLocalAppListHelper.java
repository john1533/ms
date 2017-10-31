package com.mobcent.lowest.android.ui.module.plaza.helper;

import android.content.Context;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class PlazaLocalAppListHelper {
    private String ICON = "icon";
    private String ID = "id";
    private String NAME = "name";
    private String TYPE = "type";
    private PlazaAppModel appModel = null;
    private List<PlazaAppModel> localAppModule = new ArrayList();

    public List<PlazaAppModel> getLocalPlazaAppList(Context context) {
        try {
//            InputStream inputStream = context.getApplicationContext().getClassLoader().getResourceAsStream("plaza_include_module.xml");
            InputStream inputStream = context.getAssets().open("plaza_include_module.xml");
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setContentHandler(getRootElement().getContentHandler());
            reader.parse(new InputSource(inputStream));
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.localAppModule;
    }

    private RootElement getRootElement() {
        RootElement rootElement = new RootElement("modules");
        Element moduleElement = rootElement.getChild("module");
        moduleElement.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                PlazaLocalAppListHelper.this.appModel = new PlazaAppModel();
            }
        });
        moduleElement.setEndElementListener(new EndElementListener() {
            public void end() {
                PlazaLocalAppListHelper.this.localAppModule.add(PlazaLocalAppListHelper.this.appModel);
            }
        });
        moduleElement.getChild(this.ID).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                PlazaLocalAppListHelper.this.appModel.setNativeCat(Integer.parseInt(body));
            }
        });
        moduleElement.getChild(this.TYPE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                PlazaLocalAppListHelper.this.appModel.setModelAction(Integer.parseInt(body));
            }
        });
        moduleElement.getChild(this.NAME).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                PlazaLocalAppListHelper.this.appModel.setModelName(body);
            }
        });
        moduleElement.getChild(this.ICON).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                PlazaLocalAppListHelper.this.appModel.setModelDrawable(body);
            }
        });
        return rootElement;
    }
}
