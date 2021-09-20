import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter; 

class Part3 {
	public static void main(String[] args) throws Exception {
		int run=0;
		LinkedHashMap<String,String> model = new LinkedHashMap<String,String>();///hashmap for probfuse model
		LinkedHashMap<String,Double> k_value_map = new LinkedHashMap<String,Double>();//hashmap for storing k values
		LinkedHashMap<String,Double> live_data_map = new LinkedHashMap<String,Double>();//hashmap for storing live data
		ArrayList<DocPojo> doc_list = new ArrayList<>();//ArrayList for storing live data value after probfusion
		Scanner scan_training_data = new Scanner(new File(args[0]));//scanner for getting file name from command line argument
		int sectors = Integer.parseInt(args[1]);//get number of sectors
		Scanner scan_live_data = new Scanner(new File(args[2]));//get live data on which probfuse model to be applied
		String training_data = scan_training_data.useDelimiter("\\Z").next();//save scanner data to string
		String live_data = scan_live_data.useDelimiter("\\Z").next();//store live data file data into string
		//System.out.println("\ntaining data is : \n" + training_data);
		System.out.println("\nnumber of sectors : " + sectors);
		//System.out.println("\nlive data is : \n" + live_data);
		File file = new File("part3output.txt");//out put text file to be created
		PrintWriter output_file = (new PrintWriter(new FileWriter(file)));//PrintWriter for writing data into output file
		String[] input = training_data.split("\n"); // Split and store each query in array of string
		for(int i=0; i<input.length; i++) {
			String input_data[] = input[i].split(";"); // Split query and store engine name, retrieved results and related results in array
			run = Integer.parseInt(input_data[0]); // Convert string in integer
			String engine = input_data[1]; //Extract engine name
			String retrieved_data = input_data[2]; //Extract retrieved results
			int related_result = Integer.parseInt(input_data[3].trim()); // Extract related results and convert into integer
			int total_retrieve = retrieved_data.length();//calculate length of retrieved results taht is how many documents are there
			int sector_size = total_retrieve/sectors;//calculate sector size i.e. if total documents are 10 and sectors are 5 then each sector size will 10/5 = 2
			int c = 0;
			//System.out.println("\nEngine " + engine + " Query run " + run); // Print heading
			//System.out.println("Query retrieves " + retrieved_data); // Show retrieved query
			//System.out.println("Related results " + related_result); // Show number of related results
			for(int k=1; k<=sectors; k++){//
				int rel_ret = 0;//initialize related retrieve with 0. this will increase within sector as loop revolves
				for(int j=c; j<k*sector_size; j++){//loop through each sector created
					if(retrieved_data.charAt(j)=='R') {//check if there is relevent retrieved document in sector
						rel_ret++;//increment counter when relevent retrieve document found
					}
					c++;//increment c value for each loop
				}
				double precision = (double) rel_ret / sector_size;//calculate precision for each sector
				precision = Math.round(precision*100.0)/100.0;//round of precision values
				String old_model_data = model.get(engine);//retrieve data from model hashmap
				String new_model_data = "k" + ";" + k + "=" + precision;//make key for model hashmap in such a way that it can be use to retrieve k value for live data when probfuse applied
				if(old_model_data == null){//if data is not present in model hashmap
					model.put(engine, new_model_data);//store data in hashmap
					System.out.println("k" + k + "=" + precision);//print k value each sector e.g k1=2.56
				}
				else{
					model.put(engine, old_model_data + "\n" + new_model_data);//if data is present then add new data in existing data
					System.out.println("k" + k + "=" + precision);//print k value
				}
			}
		}
		//loop through model hashmap
		for(String keys:model.keySet()){
			String[] extract_model = model.get(keys).split("\n");//retrieve all k values for perticular engine
			for(int z=0; z<extract_model.length; z++){//loop through all the k values obtained
				String[] model_values = extract_model[z].split("=");//split k value data with '=' , so that we can extract k value from it
				String key = keys+";"+model_values[0];//make a new key which contains engine name and k value e.g. 'A;k1' as key k_value_map
				double value = Double.parseDouble(model_values[1]);//get k value from model hashmap
				double old_value;
				if(k_value_map.get(key)==null)//check if value is not present
					old_value = 0.0;//initialize old value variable with 0 value
				else
					old_value = k_value_map.get(key);//if value is present then save it to old_value variable
				if(old_value==0.0)//check if old value is 0 which indicate hashmap for perticular key doesnt have any value
					k_value_map.put(key, value);//put value inside hashmap e.g. k_value_map.put("A;k1", 2.55); where 'A;k1' is key and '2.55' is value
				else
					k_value_map.put(key, old_value+value);//if value is already present add new value to already present value and put it back to hashmap
			}
		}
		//since we have k values for all queries. now we calculate average of k values
		System.out.println("\nAverage of k value\n");
		for(String key:k_value_map.keySet()){//loop through k_value_map hashmap
			double value = k_value_map.get(key);//store value
			double average = value/run;//calculate average from formula i.e total k value divided by number of queries i.e run
			double round_average = Math.round(average*100.0)/100.0;//round-off
			k_value_map.put(key, round_average);//put average k value for each engine
			System.out.println(key + "=" + round_average);//print on console
		}
		//Print all document present in live data and assign k values to them
		System.out.println("\nAll Documents");//Give heading
		System.out.println("Document	" + "Score	\n");//make table like format
		String[] live_datas_array = live_data.split("\n");//live datas are seperated by new line hence we split live_data string with '\n' to get all live data in string array
		for(int i=0; i<live_datas_array.length; i++){//loop through live data array
			String[] live_datas = live_datas_array[i].split(";");//seperate engine name and documents from live data since they are seperated with ';' hence we split them and get both data
			String engine = live_datas[0];//store engine name
			String documents[] = live_datas[1].split(",");//documents are seperated by ',' hence we split them to get each document
			int doc_length = documents.length;
			int window = doc_length/sectors;//suppose live data is 30 and sector is 4 so window value will be 7 and 4*7=28 hence last 2 values will be left we deal this issue  below
			for(int j=0; j<doc_length; j++){//loop through all documents
				String document = documents[j].replaceAll("[^0-9]","");//since first and last document contains '[' snd ']' and we want to remove it hence we have used this regex to remove unwanted characters
				int k_value = j/window+1;//calculate k_value need to be assign for each document e.g for first document j will be 0 hence (0/window+1)=0+1=1 hence we assign k1 value to document 1
				/*suppose document number is not divided equally e.g. there are 30 document but sector is 8 there for ((int) 30/8=3) and 8*3 = 24 so rest document from 25 to 30 will be left out
				//now what we will do we make last sector size bigger and accomodate left out values for that we make sure k_value should not be greater than sectors*/
				if(k_value>sectors)//check if k_value is greater than sectors
					k_value = sectors;//if k_value is greater than sector then made it equal to sectors
				String key = engine + ";" + "k" + ";" + (k_value);//make a string so that we can query k value from k_value_map and assign it to document falling inside that sector
				double value = k_value_map.get(key);//get k value for perticular segment
				double final_value = value/k_value;//divide k value by its position e.g. k1 = 1 then k1 = 1/1=1, for k2 = 0.8 final_value will be k2 = 0.8/2=0.4 similarly for k10 = 1 final value will be k10 = 1/10 = 0.1
				double round_score = Math.round(final_value*1000.0)/1000.0;//round off score upto 3 digits for better readabilty and acceptable precision
				System.out.println(document + "\t\t" + round_score);//print documents and there scores
				if(live_data_map.get(document)==null)//check if document is not already present in live_data_map
					live_data_map.put(document, round_score);//put document as a key and score as a value in live_data_map hashmap
				else {//if document is already present in hashmap
					double old_data = live_data_map.get(document);//get data already store in hashmap for perticular document
					double doc_score = old_data + round_score;//add old score and current score
					live_data_map.put(document, Math.round(doc_score*1000.0)/1000.0);//put data back to hashmap
				}
			}
		}
		//loop through live_data_map and add all values to arraylist
		for(String key:live_data_map.keySet()){
			DocPojo docpojo = new DocPojo(key, live_data_map.get(key));//create object of custom DocPojo class and pass key and valueas arguments
			doc_list.add(docpojo);
		}
		//print document in doc_list arraylist
		System.out.println("\nPerforming fusion...");
		System.out.println("Document	" + "Score	\n");
		for(int i=0; i<doc_list.size(); i++){
			System.out.println(doc_list.get(i).getDocument() + "\t\t" + doc_list.get(i).getScore());
		}
		//sort arraylist in descending order
		Collections.sort(doc_list, new Comparator<DocPojo>() {
			@Override
			public int compare(DocPojo u1, DocPojo u2) {
				return u2.getScore().compareTo(u1.getScore());
			}
		});
		
		System.out.println("\nProbfuse results");//show heading
		output_file.write("Probfuse results"); // Write probfuse in text file
		output_file.write(System.getProperty("line.separator"));//write new line in text file
		System.out.println("\nDocument	" + "Score	\n");//show heading
		output_file.write("Document\tscore");//write output values into text file
		output_file.write(System.getProperty("line.separator"));//write newline
		System.out.println("\ntotal	number of document :" + doc_list.size() + "\n");//show heading
		for(int i=0; i<doc_list.size(); i++){//loop through doc_list arraylist
			System.out.println(doc_list.get(i).getDocument() + "\t\t" + doc_list.get(i).getScore());//print document and score afte probfuse in console
			output_file.write(doc_list.get(i).getDocument() + "\t\t" + String.valueOf(doc_list.get(i).getScore())); //write probfuse output to file
			output_file.write(System.getProperty("line.separator"));//nextline in textfile
		}
		output_file.close();//close printwriter after writing in file so that it can flush old values and get ready for new values as it has limited capacity to hold data
	}
}
//create custom class for storing multiple values in arraylist by passing custom object
class DocPojo {
	private String document_number;
	private double rank_score;
	
	DocPojo(String document_number, double rank_score) {
		this.document_number = document_number;
		this.rank_score = rank_score;
	}
	
	String getDocument() {
		return document_number;
	}
	
	Double getScore() {
		return rank_score;
	}
}