package com.vmykhailyk.xsl.modules.resolvers;

import com.vmykhailyk.xsl.data.MConfig;
import org.apache.log4j.Logger;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.tools.CatalogResolver;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 25.02.11
 * Time: 14:08
 */
public class CatalogBasedResolver implements URIResolver {
    private MConfig options;
    protected static Logger logger = Logger.getLogger(CatalogBasedResolver.class);
    private CatalogResolver resolver;


    public CatalogBasedResolver(MConfig theOptions) {
        options = theOptions;
        resolver = new CatalogResolver();
        Catalog catalog = resolver.getCatalog();

/*
        try {
            catalog.parseCatalog(options.getCatalogPath());
        } catch (IOException e) {
            logger.error("Cannot read catalog file. All entries will not be resolved", e);
        }
*/
    }

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        return resolver.resolve(href, base);
    }
}
