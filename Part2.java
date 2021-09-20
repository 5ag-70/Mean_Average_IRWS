import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter; 
import java.text.DecimalFormat;

class Part2 {
	public static void main(String[] args) throws Exception {
		LinkedHashMap<String,Double> min = new LinkedHashMap<String,Double>();//hashmap for storing minimum score of a document
		LinkedHashMap<String,Double> max = new LinkedHashMap<String,Double>();//hashmap for storing maximum score of a document
		LinkedHashMap<String,Double> combSum = new LinkedHashMap<String,Double>();//hashmap for storing combsum results
		LinkedHashMap<String,Double> lcm = new LinkedHashMap<String,Double>();//hashmap for storing lcm results
		LinkedHashMap<String,Double> engine_weight_list = new LinkedHashMap<String,Double>();//hashmap for storing engine weight with their name as key
		ArrayList<String> interleaving_list = new ArrayList<>();//arraylist for interleaving documents
		ArrayList<String> new_interleaving_list = new ArrayList<>();//arraylist for interleaving documents
		ArrayList<EnginePojo> engine_results = new ArrayList<>();//arraylist for storing all engine results
		ArrayList<EnginePojo> normalized_engine_results = new ArrayList<>();//arraylist for storing all normalized engine results
		ArrayList<EnginePojo> comb_sum_list = new ArrayList<>();//arraylist for copying combSum values from hashmap so that we can sort them
		ArrayList<EnginePojo> lcm_list = new ArrayList<>();//arraylist for copying lcm result so that we can sort them
		DecimalFormat df = new DecimalFormat("#.##"); // Decimal formtter for round decimal upto two digits
		Scanner result_input = new Scanner(new File(args[0]));//scan input text files using scanner
		Scanner engine_input = new Scanner(new File(args[1]));
		String results = result_input.useDelimiter("\\Z").next();//store data from scanner in string
		String engines = engine_input.useDelimiter("\\Z").next();
		String[] engine_name = engines.split("\t");//get engine from engine list from text file
		File file = new File("part2output.txt");//output file to be generated
		PrintWriter output_file = (new PrintWriter(new FileWriter(file)));//print writer for writing data in text file
		int total_engines = engine_name.length;//get the total number of engine
		for(int i=0; i<total_engines; i++) {//iterate over engine data
			String[] engine_details = engine_name[i].split(";");//get all engine in string array
			String engine = engine_details[0];//get engine name or number
			double engine_weight = Double.parseDouble(engine_details[1]);//get engine weight
			engine_weight_list.put(engine, engine_weight);//add engine weight in hashmap with  engine name  as a key and engine weight as value
		}
		System.out.println("Engine weight list: " + engine_weight_list);//Print engine with weights
		System.out.println("\nResults :\n" + results);//print result data
		System.out.println("\nEngines :\n" + engines);//print engine data
		String[] result_line = results.split("\n");//get each line of result data in array
		for(int i=0; i<result_line.length; i++) {//iterate over array
			String[] result = result_line[i].split("\t");//extract details from array
			for(int j=0; j<result.length; j++) {
				String[] result_details = result[j].split(";");
				String engine = result_details[0];//get engine name or number
				String document_number = result_details[1];//get document number
				double rank_score = Double.parseDouble(result_details[2]);//get score of each document
				if(max.get(engine)==null)
					max.put(engine, rank_score);//store score if there is no score alredy present in max hashmap
				if(min.get(engine)==null)
					min.put(engine, rank_score);//store score if there is no score already present in min hashmap
				double max_score = max.get(engine);//retreive score from max hashmap using engine name as key
				double min_score = min.get(engine);//retrieve score from min hashmap using engine name as key
				if(rank_score>max_score)//if current obtained rank score is greater than max score present in max hashmap for perticular engine
					max.put(engine, rank_score);//replace existing maxscore with rank score so that now max hashmap hold maximum score for that perticular engine
				if(rank_score<min_score)//if current rank score is less than minimum score present in min hashmap for perticular engine
					min.put(engine, rank_score);//replace existing minimum score with current score so that min hashmap hold minimum score for that perticular engine
				EnginePojo enginePojo = new EnginePojo(engine, document_number, rank_score);//initialize EnginePojo class object with arguments
				engine_results.add(enginePojo);//add enginePojo object in engine_results arraylist
			//	if(interleaving_list!=null && !interleaving_list.contains(document_number))//check if interleaving_list hashmap contains this document or not
					interleaving_list.add(document_number);//if document is not already present in arraylist add it
				//else {
				//		interleaving_list.add("blank");
					//}
			}
		}
		for(int i=0; i<interleaving_list.size(); i++){
			String document = interleaving_list.get(i);
			String next_document = "";
			int k=3;
			if(!new_interleaving_list.contains(document))
				new_interleaving_list.add(document);
			else{
				if(interleaving_list.size()>i+k)
				next_document = interleaving_list.get(i+k);
				while(interleaving_list.size()>i+k && new_interleaving_list.contains(interleaving_list.get(i+k))){
				next_document = interleaving_list.get(i+k);
				k = k+3;
				}
				new_interleaving_list.add(i, next_document);
			}
		}
		System.out.println("\nInterleaving");//print headline of interleaving 
		output_file.write("100 Interleaving"); // write headline
		output_file.write(System.getProperty("line.separator"));
		System.out.println("\nRank\tDocument");//print column names
		output_file.write("Rank\tDocument"); // write headline
		output_file.write(System.getProperty("line.separator"));
		int count=0;
		for(int i=0; i<new_interleaving_list.size(); i++) {//loop through interleaving_list arraylist
			if(count<100){//check for 100 counts for top 100 documents
				System.out.println(i+1 + "\t" + new_interleaving_list.get(i));//print interleaving output
				output_file.write(String.valueOf(i+1) + "\t" + new_interleaving_list.get(i)); // write to text file
				output_file.write(System.getProperty("line.separator"));
			}
			count++;
		}
		System.out.println("\nmax is " + max);//print max and min hashmap
		System.out.println("min is " + min);
		for(int i=0; i<engine_results.size(); i++) {//loop through engine_results arraylist
			String engine = engine_results.get(i).getEngine();//get engine name
			String document_number = engine_results.get(i).getDocumentNumber();//get document number
			double rank_score = engine_results.get(i).getRankScore();//get rank score
			double normalized_rank_score = (rank_score - min.get(engine)) / (max.get(engine) - min.get(engine));//normalize rank score using formula 
			EnginePojo enginePojo = new EnginePojo(engine, document_number, normalized_rank_score);//create object of EnginePojo and pass above values as arguments to constructor
			normalized_engine_results.add(enginePojo);//store above normalized values to normalized_engine_results arraylist
			double old_comb_sum;
			double old_lcm;
			if(combSum.get(document_number) != null)//check if combsum value is present for perticular document or not
				old_comb_sum = combSum.get(document_number);//if value is present then store it into a variable
			else 
				old_comb_sum = 0.0;//if value is not present then initialize variable with 0
			combSum.put(document_number, old_comb_sum + normalized_rank_score);//put old combsum value and 
			if(lcm.get(document_number) != null)//check if document exist in lcm hashmap
				old_lcm = lcm.get(document_number);//if value is present store it in a variable
			else 
				old_lcm = 0.0;//if value not present then initialize variable with 0 value
			lcm.put(document_number, old_lcm + (normalized_rank_score*engine_weight_list.get(engine)));//get normalized score with engine weight and add old value to it then put all these data back to lcm hashmap
		}
		for(String key:combSum.keySet()) {//loop through combSum hashmap
			String document_number = key;//Store document number is a key for combSum hashmap
			double rank_score = combSum.get(key);//get combSum score for perticular document from combSum hashmap using document number as key
			EnginePojo enginePojo = new EnginePojo(null, document_number, rank_score);//make EnginePojo object and pass above data as arguments
			comb_sum_list.add(enginePojo);//add enginePojo object to comb_sum_list arraylist
		}
		Collections.sort(comb_sum_list, new Comparator<EnginePojo>() {//sort arraylist in descending order so that highest value will be on top
			@Override
			public int compare(EnginePojo u1, EnginePojo u2) {
				return u2.getRankScore().compareTo(u1.getRankScore());
			}
		});
		//print normalized data on console
		System.out.println("\nNormalized result ");
		int c=0;
		for(int k=0; k<normalized_engine_results.size(); k++) {
			System.out.println(normalized_engine_results.get(k).getEngine() + ";" + normalized_engine_results.get(k).getDocumentNumber() + ";" + normalized_engine_results.get(k).getRankScore() + "\t");
			c++;
			if(c>total_engines-1){
				System.out.println();
				c=0;
			}
		}
		//print combSum data to console and write it to text file
		System.out.println("\n100 Comb sum result ");
		output_file.write("\n100 Comb sum result"); // Print headline
		output_file.write(System.getProperty("line.separator"));
		System.out.println("\nRank\tDocument\tscore");
		output_file.write("Rank\tDocument\tscore"); // Print headline
		output_file.write(System.getProperty("line.separator"));
		count = 0;
		for(int k=0; k<comb_sum_list.size(); k++) {
			if(count<100){
				System.out.println(k+1 + "\t" + comb_sum_list.get(k).getDocumentNumber() + "\t\t" + df.format(comb_sum_list.get(k).getRankScore()));
				output_file.write(String.valueOf(k+1) + "\t" + comb_sum_list.get(k).getDocumentNumber() + "\t\t" + String.valueOf(df.format(comb_sum_list.get(k).getRankScore()))); // Print headline
				output_file.write(System.getProperty("line.separator"));
			}
			count++;
		}
		//add hashmap data to arraylist so that we can sort it
		for(String key:lcm.keySet()) {
			String document_number = key;
			double rank_score = lcm.get(key);
			EnginePojo enginePojo = new EnginePojo(null, document_number, rank_score);
			lcm_list.add(enginePojo);
		}
		//sort lcm data in descending order
		Collections.sort(lcm_list, new Comparator<EnginePojo>() {
			@Override
			public int compare(EnginePojo u1, EnginePojo u2) {
				return u2.getRankScore().compareTo(u1.getRankScore());
			}
		});
		//print top 100 lcm result
		System.out.println("\nLCM result");
		output_file.write("\n100 LCM result");
		output_file.write(System.getProperty("line.separator"));
		System.out.println("\nRank\tDocument\tscore");
		output_file.write("Rank\tDocument\tscore");
		output_file.write(System.getProperty("line.separator"));
		count = 0;
		for(int k=0; k<lcm_list.size(); k++) {
			if(count<100){
				System.out.println(k+1 + "\t" + lcm_list.get(k).getDocumentNumber() + "\t\t" + df.format(lcm_list.get(k).getRankScore()));
				output_file.write(String.valueOf(k+1) + "\t" + lcm_list.get(k).getDocumentNumber() + "\t\t" + String.valueOf(df.format(lcm_list.get(k).getRankScore()))); // Print headline
				output_file.write(System.getProperty("line.separator"));
			}
			count++;
		}
		output_file.close();//close PrintWriter so that it can flush existing data from memory and get ready to take new data
	}
}
//create custom class for storing multiple values in arraylist by passing custom object
class EnginePojo {
	String engine;
	private String document_number;
	private double rank_score;
	
	EnginePojo(String engine, String document_number, double rank_score) {
		this.engine = engine;
		this.document_number = document_number;
		this.rank_score = rank_score;
	}
	
	String getEngine() {
		return engine;
	}
	
	String getDocumentNumber() {
		return document_number;
	}
	
	Double getRankScore() {
		return rank_score;
	}
}