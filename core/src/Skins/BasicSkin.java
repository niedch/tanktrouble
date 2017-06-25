package Skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import Utils.Constants;

public class BasicSkin extends Skin{
	
	public BasicSkin() {
		//Create a font
		BitmapFont font = new BitmapFont();
		this.add("default", font);

		//Add Regions
		TextureAtlas volumeAtlas = new TextureAtlas(Constants.General.BUTTON_PACK);
		this.addRegions(volumeAtlas);

		//Create a texture
		Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		this.add("background",new Texture(pixmap));

		pixmap = new Pixmap((int) 4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		this.add("cursor",new Texture(pixmap));

		//Create a button style
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = this.newDrawable("background", Color.GRAY);
		textButtonStyle.down = this.newDrawable("background", Color.DARK_GRAY);
		textButtonStyle.font = this.getFont("default");
		this.add("default", textButtonStyle);

		TextButton.TextButtonStyle volumeStyle = new TextButton.TextButtonStyle();
		volumeStyle.up = this.getDrawable("volume");
		volumeStyle.checked = this.getDrawable("mute");
		volumeStyle.font = this.getFont("default");
		this.add("volume",volumeStyle);

		//List style
		List.ListStyle listStyle = new List.ListStyle();
		listStyle.font = this.getFont("default");
		listStyle.fontColorSelected = Color.WHITE;
		listStyle.fontColorUnselected = Color.GRAY;
		listStyle.selection = this.newDrawable("background", Color.RED);
		this.add("default", listStyle);

		//ScrollPane style
		ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
		scrollPaneStyle.hScrollKnob = this.newDrawable("background",Color.DARK_GRAY);
		scrollPaneStyle.vScrollKnob = this.newDrawable("background",Color.DARK_GRAY);
		this.add("default", scrollPaneStyle);


		//Label style
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.background = this.newDrawable("background", Color.WHITE);
		labelStyle.font = this.getFont("default");
		labelStyle.fontColor = Color.DARK_GRAY;
		this.add("default", labelStyle);

		Label.LabelStyle labelStyle2 = new Label.LabelStyle();
		labelStyle2.background = this.newDrawable("background", Color.RED);
		labelStyle2.font = this.getFont("default");
		labelStyle2.fontColor = Color.DARK_GRAY;
		this.add("myPlayer", labelStyle2);

		Label.LabelStyle labelStyle3 = new Label.LabelStyle();
		labelStyle3.background = this.newDrawable("background", Color.RED);
		labelStyle3.font = this.getFont("default");
		labelStyle3.fontColor = Color.DARK_GRAY;
		this.add("playerRed", labelStyle3);

		Label.LabelStyle labelStyle4 = new Label.LabelStyle();
		labelStyle4.background = this.newDrawable("background", Color.GREEN);
		labelStyle4.font = this.getFont("default");
		labelStyle4.fontColor = Color.DARK_GRAY;
		this.add("playerGreen", labelStyle4);

		//Textfield style
		TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
		textFieldStyle.background = this.newDrawable("background", Color.DARK_GRAY);
		textFieldStyle.font = this.getFont("default");
		textFieldStyle.fontColor = Color.WHITE;
		textFieldStyle.cursor = this.newDrawable("cursor", Color.WHITE);
		textFieldStyle.selection = this.newDrawable("background",Color.BLUE);
		this.add("default",textFieldStyle);
	}
}