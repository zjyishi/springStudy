package tacos.data;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;
import tacos.Taco;
import tacos.data.IngredientRepository;
@Repository
public class JdbcTacoRepository implements TacoRepository{
	private JdbcTemplate jdbc;
//	private final IngredientRepository ingredientRepo;
	@Autowired
	public JdbcTacoRepository(JdbcTemplate jdbc){
		this.jdbc = jdbc;
//		this.ingredientRepo = ingredientRepo;
	}
	
	@Override
	public Taco save(Taco taco){
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);
//		for(String ingredientName : taco.getIngredients()){
//			Ingredient ingredient = ingredientRepo.findOneByName(ingredientName);
		for(Ingredient ingredient : taco.getIngredients()){
			saveIngredientToTaco(ingredient, tacoId);
		}
		return taco;
	}
	private long saveTacoInfo(Taco taco){
		taco.setCreatedAt(new Date());
		PreparedStatementCreator psc = 
				new PreparedStatementCreatorFactory(
						"insert into Taco (name, createdAt) values(?, ?)", 
						Types.VARCHAR, Types.TIMESTAMP
						).newPreparedStatementCreator(
								Arrays.asList(
										taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc, keyHolder);
		return keyHolder.getKey().longValue();
	}
	private void saveIngredientToTaco(Ingredient ingredient, long tacoId){
		jdbc.update(
				"insert into Taco_Ingredients (taco, ingredient) values (?, ?)", 
				tacoId, ingredient.getId());
	}
}
