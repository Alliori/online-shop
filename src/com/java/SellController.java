package com.java;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import com.java.model.Customer;
import com.java.model.Item;

@Named
@RequestScoped
public class SellController implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final double MAX_IMAGE_WIDTH = 400;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction userTrans;
	
	private Part part;

	@Inject
	private Item item;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String persist(SignInController signinController) {
		try {
			userTrans.begin();
			InputStream input = part.getInputStream();
			ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
			byte[]buffer =new byte[51200];
			for(int length =0; (length=input.read(buffer))>0;) {
				outputStream.write(buffer,0,length);
			}
			item.setFoto(scale(outputStream.toByteArray()));
			
			Customer customer = signinController.getCustomer();
			
			customer = em.find(Customer.class, customer.getId());
			
			item.setSeller(customer);
			em.persist(item);
			userTrans.commit();
			Logger.getLogger(SellController.class.getCanonicalName())
				.log(Level.INFO, "Saving: " + item);
			FacesMessage m = new FacesMessage(
					"Succesfully saved " +
					item);
			FacesContext
				.getCurrentInstance()
				.addMessage("sellForm", m);
		} catch (Exception e) {


		}
		return "sell";
	}
	public byte[] scale(byte[] foto) throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(foto);
		BufferedImage originalBufferedImage = ImageIO.read(byteArrayInputStream);	        		
		
		double originalWidth = (double) originalBufferedImage.getWidth();
		double originalHeight = (double) originalBufferedImage.getHeight();
		double relevantWidth = originalHeight > originalWidth  ? originalHeight : originalWidth; 
		
		double transformationScale = MAX_IMAGE_WIDTH / relevantWidth;
		int width = (int) Math.round( transformationScale * originalWidth );
		int height = (int) Math.round( transformationScale * originalHeight );

		BufferedImage resizedBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = resizedBufferedImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		AffineTransform affineTransform = AffineTransform.getScaleInstance(transformationScale, transformationScale);
		g2d.drawRenderedImage(originalBufferedImage, affineTransform);
        
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(resizedBufferedImage, "PNG", baos);
		return baos.toByteArray();
	}


}
