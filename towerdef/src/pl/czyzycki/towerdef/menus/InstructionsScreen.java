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
		
		addLabel(paneTable, "Twoj� baz� atakuj� roboty z innej galaktyki.");
		paneTable.row();
		addImage(paneTable, "ground-unit");
		paneTable.row();
		addImage(paneTable, "airborne-unit");
		paneTable.row();
		
		addLabel(paneTable, "MENU BUDYNK�W");
		paneTable.row();
		addLabel(paneTable, "Opracowali�my kilka budynk�w obronnych,\nkt�re mo�esz wybra� z menu na dole ekranu.");
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
		
		addLabel(paneTable, "RODZAJE BUDYNK�W");
		paneTable.row();
		addLabel(paneTable, "Produkujemy 3 typy budynk�w przeciwko jednostkom:");
		paneTable.row();
		
		Table typeTable = new Table(skin);
		typeTable.width = stage.width();
		typeTable.height = stage.height();
		
		addLabel(typeTable, "a). naziemnym");
		addImage(typeTable, "point-tower-ground");
		
		typeTable.row();
		
		addLabel(typeTable, "b). lataj�cym");
		addImage(typeTable, "point-tower-airborne");
		
		typeTable.row();
		
		addLabel(typeTable, "c). naziemnym i lataj�cym");
		addImage(typeTable, "point-tower-both");
		
		paneTable.add(typeTable);
		
		paneTable.row();
		addSpace(paneTable, 50);
		addLabel(paneTable, "Mamy 4 rodzaje wie�yczek:");
		paneTable.row();
		addLabel(paneTable, "LASER\nStrzela szybko, ale zadaje niewielkie obra�enia.");
		paneTable.row();
		addImage(paneTable, "button-point-tower");
		paneTable.row();
		addLabel(paneTable, "OBSZAR�WKA\nZadaje obra�enia wszystkim jednostkom\nkt�re znajduj� si� w zasi�gu.");
		paneTable.row();
		addImage(paneTable, "button-area-tower");
		paneTable.row();
		addLabel(paneTable, "WYRZUTNIA RAKIET\nStrzela rakietami, kt�re po dotarciu do celu\ndodatkowo eksploduj� i zadaj� obra�enia\njednostkom b�d�cym w zasi�gu wybuchu.");
		paneTable.row();
		addImage(paneTable, "button-bullet-tower");
		paneTable.row();
		addLabel(paneTable, "SPOWALNIACZ\nSpowalnia jednostki b�d�ce w zasi�gu.\nDzia�a zar�wno na jednostki lataj�ce i naziemne.");
		paneTable.row();
		addImage(paneTable, "button-slowdown-tower");
		paneTable.row();
		
		addSpace(paneTable, 50);
		
		addLabel(paneTable, "ULEPSZANIE BUDYNK�W");
		paneTable.row();
		addLabel(paneTable, "�eby przej�� do menu ulepsze�\nwci�nij interesuj�c� Ci� wie�yczk�.\nMo�esz ulepsza� takie parametry jak:");
		paneTable.row();
		
		Table upgradeTable = new Table(skin);
		upgradeTable.width = stage.width();
		upgradeTable.height = stage.height();
		
		addLabel(upgradeTable, "Zasi�g");
		addImage(upgradeTable, "upgrade-range");
		upgradeTable.row();
		addLabel(upgradeTable, "Zadawane obra�enia");
		addImage(upgradeTable, "upgrade-damage");
		upgradeTable.row();
		addLabel(upgradeTable, "Szybko�� prze�adowania");
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
		addLabel(paneTable, "Co jaki� czas po zniszczeniu jednostki\nna planszy pojawiaj� si� bonusy:");
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
		
		addLabel(paneTable, "�eby u�y� bonus kt�ry nie zu�ywa si� zaraz po jego\nzebraniu musisz skorzysta� z menu po lewej stronie:");
		paneTable.row();
		
		Table leftTable = new Table(skin);
		leftTable.width = stage.width();
		leftTable.height = stage.height();
		
		addLabel(leftTable, "�eby aktywowa� maksymalne ulepszenie\npo prostu je wci�nij.");
		addImage(leftTable, "maxUpgradeButton");
		leftTable.row();
		
		addLabel(leftTable, "�eby spu�ci� bomb�\nprzeci�gnij j� na plansz�.");
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
