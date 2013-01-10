package pl.czyzycki.towerdef.menus;

import pl.czyzycki.towerdef.TowerDef;
import pl.czyzycki.towerdef.TowerDef.GameSound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.esotericsoftware.tablelayout.Cell;

/**
 * Ekran instrukcji dla gracza.
 *
 */
public class InstructionsScreen extends MenuBaseScreen {
	
	public InstructionsScreen(TowerDef game) {
		super(game, false);
	}

	private Label addLabel(Table paneTable, String text) {
		LabelStyle st = getSkin().getStyle("small", LabelStyle.class);
		Label label = new Label(text, st);
		paneTable.add(label);
		label.setAlignment(Align.CENTER);
		return label;
	}
	
	private Image addImage(Table paneTable, String name) {
		TextureAtlas texAtlas = TowerDef.getGame().getAssetManager().get("images/objects.pack", TextureAtlas.class);
		Image img = new Image(texAtlas.createSprite(name));
		paneTable.add(img);
		return img;
	}
	
	private void addSpace(Table paneTable, int spacing) {
		Actor a = null;
		paneTable.row();
		@SuppressWarnings("unchecked")
		Cell<Actor> c = paneTable.add(a);
		c.height(spacing);
		paneTable.row();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		Skin skin = getSkin();
		
		Table table = new Table(skin);
		table.width = stage.width();
		table.height = stage.height();
		
		stage.addActor(table);
		
		TableLayout layout = table.getTableLayout();
		
		FlickScrollPane pane = new FlickScrollPane();
		layout.register("scrollPane", pane);
		
		Table paneTable = new Table(skin);
		pane.setWidget(paneTable);

		
		// --------------------------------------------------------------------
		addLabel(paneTable, "CEL GRY");
		paneTable.row();
		addLabel(paneTable, "Twoim celem jest obrona bazy.");
		paneTable.row();
		addImage(paneTable, "base");
		paneTable.row();
		
		addLabel(paneTable, "Twoj¹ bazê atakuj¹ roboty z innej galaktyki.");
		paneTable.row();
		addImage(paneTable, "ground-unit");
		paneTable.row();
		addImage(paneTable, "airborne-unit");
		paneTable.row();
		
		addLabel(paneTable, "MENU BUDYNKÓW");
		paneTable.row();
		addLabel(paneTable, "Opracowaliœmy kilka budynków obronnych,\nktóre mo¿esz wybraæ z menu na dole ekranu.");
		paneTable.row();
		Table iconTable = new Table(skin);
		iconTable.width = stage.width();
		iconTable.height = stage.height();
		addImage(iconTable, "button-area-tower");
		addImage(iconTable, "button-point-tower");
		addImage(iconTable, "button-bullet-tower");
		addImage(iconTable, "button-slowdown-tower");
		paneTable.add(iconTable);
		paneTable.row();
		
		addSpace(paneTable, 50);
		
		addLabel(paneTable, "RODZAJE BUDYNKÓW");
		paneTable.row();
		addLabel(paneTable, "Produkujemy 3 typy budynków przeciwko jednostkom:");
		paneTable.row();
		
		Table typeTable = new Table(skin);
		typeTable.width = stage.width();
		typeTable.height = stage.height();
		
		addLabel(typeTable, "a). naziemnym");
		addImage(typeTable, "point-tower-ground");
		
		typeTable.row();
		
		addLabel(typeTable, "b). lataj¹cym");
		addImage(typeTable, "point-tower-airborne");
		
		typeTable.row();
		
		addLabel(typeTable, "c). naziemnym i lataj¹cym");
		addImage(typeTable, "point-tower-both");
		
		paneTable.add(typeTable);
		
		paneTable.row();
		addSpace(paneTable, 50);
		addLabel(paneTable, "Mamy 4 rodzaje wie¿yczek:");
		paneTable.row();
		addLabel(paneTable, "LASER\nStrzela szybko, ale zadaje niewielkie obra¿enia.");
		paneTable.row();
		addImage(paneTable, "button-point-tower");
		paneTable.row();
		addLabel(paneTable, "OBSZARÓWKA\nZadaje obra¿enia wszystkim jednostkom\nktóre znajduj¹ siê w zasiêgu.");
		paneTable.row();
		addImage(paneTable, "button-area-tower");
		paneTable.row();
		addLabel(paneTable, "WYRZUTNIA RAKIET\nStrzela rakietami, które po dotarciu do celu\ndodatkowo eksploduj¹ i zadaj¹ obra¿enia\njednostkom bêd¹cym w zasiêgu wybuchu.");
		paneTable.row();
		addImage(paneTable, "button-bullet-tower");
		paneTable.row();
		addLabel(paneTable, "SPOWALNIACZ\nSpowalnia jednostki bêd¹ce w zasiêgu.\nDzia³a zarówno na jednostki lataj¹ce i naziemne.");
		paneTable.row();
		addImage(paneTable, "button-slowdown-tower");
		paneTable.row();
		
		addSpace(paneTable, 50);
		
		addLabel(paneTable, "ULEPSZANIE BUDYNKÓW");
		paneTable.row();
		addLabel(paneTable, "¯eby przejœæ do menu ulepszeñ\nwciœnij interesuj¹c¹ Ciê wie¿yczkê.\nMo¿esz ulepszaæ takie parametry jak:");
		paneTable.row();
		
		Table upgradeTable = new Table(skin);
		upgradeTable.width = stage.width();
		upgradeTable.height = stage.height();
		
		addLabel(upgradeTable, "Zasiêg");
		addImage(upgradeTable, "upgrade-range");
		upgradeTable.row();
		addLabel(upgradeTable, "Zadawane obra¿enia");
		addImage(upgradeTable, "upgrade-damage");
		upgradeTable.row();
		addLabel(upgradeTable, "Szybkoœæ prze³adowania");
		addImage(upgradeTable, "upgrade-cooldown");
		upgradeTable.row();
		addLabel(upgradeTable, "Spowolnienie");
		addImage(upgradeTable, "upgrade-multiplier");
		upgradeTable.row();
		paneTable.add(upgradeTable);
		paneTable.row();
		
		addSpace(paneTable, 50);
		
		addLabel(paneTable, "BONUSY");
		paneTable.row();
		addLabel(paneTable, "Co jakiœ czas po zniszczeniu jednostki\nna planszy pojawiaj¹ siê bonusy:");
		paneTable.row();
		
		Table bonusTable = new Table(skin);
		bonusTable.width = stage.width();
		bonusTable.height = stage.height();
		
		addLabel(bonusTable, "Dodatkowa kasa");
		addImage(bonusTable, "moneyBonus");
		bonusTable.row();
		addLabel(bonusTable, "Maksymalne ulepszenie\n na 10 sekund.");
		addImage(bonusTable, "maxUpgrade");
		bonusTable.row();
		addLabel(bonusTable, "Bomba");
		addImage(bonusTable, "bomb");
		
		paneTable.add(bonusTable);
		paneTable.row();
		
		addSpace(paneTable, 50);
		
		addLabel(paneTable, "¯eby u¿yæ bonus który nie zu¿ywa siê zaraz po jego\nzebraniu musisz skorzystaæ z menu po lewej stronie:");
		paneTable.row();
		
		Table leftTable = new Table(skin);
		leftTable.width = stage.width();
		leftTable.height = stage.height();
		
		addLabel(leftTable, "¯eby aktywowaæ maksymalne ulepszenie\npo prostu je wciœnij.");
		addImage(leftTable, "maxUpgradeButton");
		leftTable.row();
		
		addLabel(leftTable, "¯eby spuœciæ bombê\nprzeci¹gnij j¹ na planszê.");
		addImage(leftTable, "bombButton");
		leftTable.row();
		
		paneTable.add(leftTable);
		paneTable.row();
		Table pauseTable = new Table(skin);
		pauseTable.width = stage.width();
		pauseTable.height = stage.height();
		
		addSpace(paneTable, 50);
		
		addLabel(paneTable, "DODATKOWE PRZYCISKI");
		paneTable.row();
		addLabel(pauseTable, "Menu pauzy");
		addImage(pauseTable, "pause");
		pauseTable.row();
		addLabel(pauseTable, "Przyspieszenie 2x");
		addImage(pauseTable, "faster");
		pauseTable.row();
		
		paneTable.add(pauseTable);
		
		addSpace(paneTable, 50);
		
		paneTable.row();
		addLabel(paneTable, "POWODZENIA!");
		
		// --------------------------------------------------------------------
		
		TextButton backButton = new TextButton("OK", skin);
		backButton.setClickListener( new ClickListener() {
            @Override
            public void click(Actor actor, float x, float y )
            {
            	game.playSound(GameSound.CLICK);
                game.setScreen(game.getMainMenuScreen());
            }
        } );
		layout.register("backButton", backButton);
		
		layout.parse(Gdx.files.internal( "layouts/instructions.txt" ).readString("UTF-8"));
	}

	@Override
	public void backDown() {
		game.setScreen(game.getMainMenuScreen());
	}
}
