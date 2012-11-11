package pl.czyzycki.towerdef.gameplay;

import java.util.Iterator;

import pl.czyzycki.towerdef.gameplay.entities.Enemy;
import pl.czyzycki.towerdef.gameplay.entities.Spawn;
import pl.czyzycki.towerdef.gameplay.helpers.MapChecker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;

class GameplayLoader {

	final GameplayScreen screen;
	
	Json json;
	OrderedMap<String, Enemy> enemiesDatabase;
	String lastMap;
	
	@SuppressWarnings("unchecked")
	GameplayLoader(GameplayScreen screen) {
		this.screen = screen;
		json = new Json();
		enemiesDatabase = json.fromJson(OrderedMap.class, Enemy.class, Gdx.files.internal("config/enemies.json"));
	}
	
	@SuppressWarnings("unchecked")
	public void loadMap(String mapName) {
		lastMap = mapName;
		
		TiledMap map = screen.map = TiledLoader.createMap(Gdx.files.internal("maps/" + mapName + ".tmx"));
		screen.tileMapRenderer = new TileMapRenderer(map, screen.game.getTileAtlas(), 16, 11);
		
		OrderedMap<String, Object> mapData = (OrderedMap<String, Object>)new JsonReader().parse(Gdx.files.internal("maps/" + mapName + ".json"));
		json.readField(screen, "base", mapData);
		json.readField(screen, "wavesLeft", "waves", mapData);
		json.readField(screen, "money", "money", mapData);
		
		boolean[][] buildingMap = new boolean[map.height][map.width];
		boolean[][] roadMap = new boolean[map.height][map.width];
		
		int[][] marks = map.layers.get(3).tiles;
		for(int r = 0; r < map.height; ++r) {
			for(int c = 0; c < map.width; ++c) {
				if(marks[r][c] > 0) {
					buildingMap[r][c] = true;
					switch(marks[r][c]) {
					case 1:
						Spawn.Border border = Spawn.Border.LEFT;
						boolean found = true;
						if(c == 0) border = Spawn.Border.LEFT;
						else if(c == map.width - 1) border = Spawn.Border.RIGHT;
						else if(r == 0) border = Spawn.Border.TOP;
						else if(r == map.height - 1) border = Spawn.Border.BOTTOM;
						else found = false;
						if(found) screen.spawns.add(new Spawn(c*GameplayScreen.tileWidth+GameplayScreen.tileWidth/2f,
								(map.height-r)*GameplayScreen.tileHeight-GameplayScreen.tileHeight/2f, border, screen));
						else Gdx.app.error("Spawn creation failed:","spawn is not on a border of the map");
						break;
					case 2:
						screen.base.init(c*GameplayScreen.tileWidth+GameplayScreen.tileWidth/2f,
								(map.height-r)*GameplayScreen.tileHeight-GameplayScreen.tileHeight/2f);
						roadMap[r][c] = true;
						break;
					case 17:
						roadMap[r][c] = true;
					}
				} else {
					buildingMap[r][c] = false;
					roadMap[r][c] = false;
				}
			}
		}
		
		screen.buildingChecker = new MapChecker(buildingMap, map.width, map.height, true);
		screen.roadChecker = new MapChecker(roadMap, map.width, map.height, false);
		
		Array<OrderedMap<String,Object>> spawnsConfig = (Array<OrderedMap<String, Object>>)mapData.get("spawns");
		Iterator<OrderedMap<String, Object>> spawnsIterator = spawnsConfig.iterator();
		
		for(Spawn spawn : screen.spawns) {
			spawn.calculatePaths(screen.roadChecker, screen.base.getPos());
			for(OrderedMap<String, Object> turn : (Array<OrderedMap<String,Object>>)spawnsIterator.next().get("turns")) {
				spawn.addWave();
				for(OrderedMap<String,Object> enemy : (Array<OrderedMap<String,Object>>)turn.get("enemies")) {
					spawn.addEnemy(new Enemy(enemiesDatabase.get((String)enemy.get("name")), (Float)enemy.get("timer")));
				}
			}
			spawn.startWave();
		}
	}
}
