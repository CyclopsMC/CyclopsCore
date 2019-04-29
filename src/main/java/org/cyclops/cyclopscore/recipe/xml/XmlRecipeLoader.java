package org.cyclops.cyclopscore.recipe.xml;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * An XML Recipe loader.
 * @author rubensworks
 */
public class XmlRecipeLoader {

	private final RecipeHandler recipeHandler;

    @Getter private final ModBase mod;
    private final StreamSource stream;
    private final String fileName;
    private InputStream xsdIs = null;
    private Document doc = null;

	/**
	 * Make a new loader for the given file.
	 * @param mod The mod.
	 * @param is The stream containing xml recipes.
	 * @param fileName The file name, used for debugging.
     * @param recipeHandler The handler.
	 */
	public XmlRecipeLoader(ModBase mod, InputStream is, String fileName, RecipeHandler recipeHandler) {
		this.mod = mod;
		this.stream = new StreamSource(is);
		this.fileName = fileName;
        this.recipeHandler = recipeHandler;
	}
	
	/**
	 * Set the XSD validator.
	 * @param xsdIs The inputstream for the validator.
	 */
	public void setValidator(InputStream xsdIs) {
		this.xsdIs = xsdIs;
	}
	
	/**
	 * Validate the xml file.
	 * @throws XmlRecipeException If the file was invalid.
	 */
	public void validate() throws XmlRecipeException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			if(xsdIs != null) {
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				factory.setErrorHandler(new ErrorHandler() {
					
					@Override
					public void warning(SAXParseException exception) throws SAXException {
						getMod().log(Level.WARN, "[" + fileName + "]: " + exception.getMessage());
					}
					
					@Override
					public void fatalError(SAXParseException exception) throws SAXException {
                        getMod().log(Level.FATAL, "[" + fileName + "]: " + exception.getMessage());
					}
					
					@Override
					public void error(SAXParseException exception) throws SAXException {
                        getMod().log(Level.ERROR, "[" + fileName + "]: " + exception.getMessage());
					}
				});
		        Schema schema = factory.newSchema(new StreamSource(xsdIs));
				dbFactory.setSchema(schema);
			}
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputStream is = stream.getInputStream();
            if(is == null) {
                throw new XmlRecipeException("The recipe file " + fileName + " was not found for this mod.");
            }
			doc = dBuilder.parse(is);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new XmlRecipeException("The recipe file " + fileName + " was invalid: " + e.getMessage());
		}
    }
	
	/**
	 * Load the recipes for this instance.
     * @param crashOnInvalidRecipe If the loader should crash when an invalid recipe has been found.
     *                             Will skip recipe otherwise.
	 * @throws XmlRecipeException If something went wrong.
	 */
	public void loadRecipes(boolean crashOnInvalidRecipe) throws XmlRecipeException {
		if(doc == null) {
			validate();
		}
		
		NodeList recipes = doc.getElementsByTagName("recipe");
		for(int i = 0; i < recipes.getLength(); i++) {
			Element recipe = (Element) recipes.item(i);
			if(isRecipeEnabled(recipe)) {
                try {
                    handleRecipe(recipe);
                } catch (XmlRecipeException e) {
                    if(crashOnInvalidRecipe) {
                        throw e;
                    } else {
                        getMod().log(Level.ERROR, e.getMessage());
                    }
                }
			}
		}
	}
	
	private boolean isRecipeEnabled(Element recipe) {
		boolean enable = true;
		NodeList conditions = recipe.getElementsByTagName("condition");
		int j = 0;
		while(j < conditions.getLength() && enable) {
			Node condition = conditions.item(j);
			String conditionType = condition.getAttributes().getNamedItem("type").getTextContent();
			IRecipeConditionHandler handler = recipeHandler.getRecipeConditionHandlers().get(conditionType);
			if(handler == null) {
				throw new XmlRecipeException(String.format(
						"Could not find a recipe condition handler of type '%s'", conditionType));
			}
			String param = condition.getTextContent();
			enable = handler.isSatisfied(recipeHandler, param);
			j++;
		}
		return enable;
	}
	
	private void handleRecipe(Element recipe) throws XmlRecipeException {
		String type = recipe.getAttributes().getNamedItem("type").getTextContent();
		IRecipeTypeHandler handler = recipeHandler.getRecipeTypeHandlers().get(type);
		if(handler == null) {
			throw new XmlRecipeException(String.format(
					"Could not find a recipe type handler of type '%s'", type));
		}
		IRecipe recipeHolder = handler.loadRecipe(recipeHandler, recipe);
		if(recipeHolder != null) {
			for (String tag : getTags(recipe)) {
				getMod().getRecipeHandler().getTaggedRecipes().put(handler.getCategoryId() + ":" + tag, recipeHolder);
			}
		}
    }

    private List<String> getTags(Element recipe) {
        NodeList tagNodes = recipe.getElementsByTagName("tag");
        List<String> tags = Lists.newArrayListWithCapacity(tagNodes.getLength());
        for (int i = 0; i < tagNodes.getLength(); i++) {
            Element tag = (Element) tagNodes.item(i);
            tags.add(tag.getTextContent());
        }
        return tags;
    }
	
	/**
	 * Error that can occur while reading xml recipes.
	 * @author rubensworks
	 */
	public static class XmlRecipeException extends RuntimeException {
		
		/**
		 * Make a new instance.
		 * @param message The message.
		 */
		public XmlRecipeException(String message) {
			super(message);
		}
		
		/**
		 * Make a new instance.
		 * @param e The exception with a message.
		 */
		public XmlRecipeException(Exception e) {
			super(e.getMessage());
		}
		
	}
	
}
