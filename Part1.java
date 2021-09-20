import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;

class Part1 {
	public static void main(String[] args) throws Exception {
		LinkedHashMap<String,String> all_average_precision = new LinkedHashMap<String,String>();// hashmap used to store average precison values for each engine
		LinkedHashMap<Integer,Double> inverted_index = new LinkedHashMap<Integer,Double>();// hashmap to store precision and recall for inverted index
		ArrayList<Double> precision_array = new ArrayList<>(); // Array list for storing all precisions value and store zero for non recall points
		ArrayList<Double> precisions = new ArrayList<>(); // Array list for storing precisios only at recall points for inverted index
		ArrayList<Double> precision_at_recall = new ArrayList<>(); // Array list for storing precisions only at recall point
		ArrayList<Double> precision_at = new ArrayList<>(); // ArrayList for storing precision at all points
		ArrayList<Double> recalls = new ArrayList<>(); // Array list for storing recall at recall points
		ArrayList<PrecisionPojo> inverted_index_array = new ArrayList<>(); // Array list for storing recall at recall points
		DecimalFormat df = new DecimalFormat("#.##"); // Decimal formtter for round decimal upto two digits
		Scanner scanner_input = new Scanner(new File(args[0])); // Scanner for getting input file
		String input_words = scanner_input.useDelimiter("\\Z").next(); // Add scanner input into string
		System.out.println(input_words); // Print input file text
		String[] queries = input_words.split("\n"); // Split and store each query in array of string
		File file = new File("part1output.txt");//output file
		PrintWriter output_file = (new PrintWriter(new FileWriter(file)));//Printwriter for writing data in output file
		for(int i=0; i<queries.length; i++) {
			precision_array.clear(); // Clear previous values stored in array if any
			precisions.clear();
			precision_at.clear();
			precision_at_recall.clear();
			recalls.clear();
			inverted_index_array.clear();
			precision_at_recall.add(0.0);//add zero recall value to zero position in arraylist so that we will add next value at first position
			String query[] = queries[i].split(";"); // Split query and store engine name, retrieved results and related results in array
			int run = Integer.parseInt(query[0]); // Convert string in integer
			String engine = query[1]; //Extract engine name
			String retrieve = query[2]; //Extract retrieved results
			int related_result = Integer.parseInt(query[3].trim()); // Extract related results and convert into integer
			System.out.println("Engine " + engine + " Query run " + run); // Print headline
			output_file.write("Engine " + engine + " Query run " + run); // Write headline in output file
			output_file.write(System.getProperty("line.separator"));//add next line in output file
			System.out.println("Query retrieves " + retrieve); // Show retrieved query
			System.out.println("Related results " + related_result); // Show number of related results
			int rel_ret = 0;
			int total_ret = 0;
			double precision = 0;
			double recall = 0;
			double all_precision = 0;
			double average_precision = 0;
			int rel_ret_at_recall = 0;
			for(int j=0; j<retrieve.length(); j++) {//iterate over the retrieve results
				total_ret++;
				if(retrieve.charAt(j)=='R') {//find R in retrieve results
					rel_ret++;
					precision = (double) rel_ret / total_ret;//calculate precision
					recall = (double) rel_ret / related_result;//calculate recall
					precision_array.add(j, precision);//add precision in array list
					precisions.add(precision);
					precision_at_recall.add(precision);
					recalls.add(recall*100);//add recall value to arraylist
				}
				else {
					precision_array.add(j, 0.0);//if R not found then also calculate precision and store in different arraylist
				}
				precision_at.add((double) rel_ret / total_ret);//precision at every point
			}
			for(int k=0; k<precision_array.size(); k++) {//total precision
				all_precision = all_precision + precision_array.get(k); // show values
			}
			average_precision = all_precision / related_result;//find average precision
			String old_value = all_average_precision.get(engine);//check if any value exist in hashmap
			if(old_value==null)//if no value is there for perticular engine
				all_average_precision.put(engine, String.valueOf(df.format(average_precision)));//add average precision for perticular engine
			else
				all_average_precision.put(engine, old_value + "," + String.valueOf(df.format(average_precision)));//if value already exist add new calculated value to already present value

			//precision at 0.5 recall value i.e. where (rel_ret/related_result=0.5), since we know related_result and recall value hence we can find rel_ret
			rel_ret_at_recall = (int) (0.5 * related_result);	//find point where recall value is given
			double p_r = 0.0;
			if(rel_ret_at_recall > precision_at_recall.size()-1)	//if precision value is not  present at that recall point
				p_r = 0.0;//show precision zero
			else p_r = precision_at_recall.get(rel_ret_at_recall);//if precision value present at perticular recall point get that value
			String precision_final = df.format((double) rel_ret / total_ret);//format values before printing so that values print upto 2 decimals
			String recall_final = df.format((double) rel_ret / related_result);//calculate total recall
			System.out.println("total retrieves		=	" + total_ret);	//print values retrieve from engine results
			System.out.println("related retrieves	=	" + rel_ret);//show how many related retrieve documents are there
			System.out.println("Precision		=	" + precision_final);	//show total precision
			output_file.write("Precision : " + precision_final);//write total precision in text file
			output_file.write(System.getProperty("line.separator"));
			System.out.println("Recall			=	" + recall_final);//show total recall value of query run
			output_file.write("Recall : " + recall_final);//write in text file
			output_file.write(System.getProperty("line.separator"));
			System.out.println("P@5			=	" + df.format(precision_at.get(4)));//show precision at 5th rec point, since arraylist starts with 0 we use 4 wehn 5th position is required
			output_file.write("P@5 : " + df.format(precision_at.get(4)));
			output_file.write(System.getProperty("line.separator"));
			System.out.println("P@R=0.5			=	" + df.format(p_r));//show precision at recall value 0.5
			output_file.write("P@R=0.5 : " + df.format(p_r));
			output_file.write(System.getProperty("line.separator"));
			System.out.println("Average precision = " + df.format(average_precision));//print average precision
			output_file.write("Average precision = " + df.format(average_precision));
			output_file.write(System.getProperty("line.separator"));
			System.out.println("Inverted index:");//print title as inverted index
			output_file.write("Inverted index: \n");
			for(int pos=100; pos>=0; ){//for inverted index for 11 values from 100 to 0
				inverted_index.put(pos, 0.0);//create a hashmap with key as recall points and 0.0 default precision values
				pos = pos-10;
			}
			double old_precision = 0.0;
			for(int x=precisions.size()-1; x>=0; x--) {//check if bigger precision value is present then overwrite small values
				if(old_precision > precisions.get(x))
					precisions.set(x, old_precision);
				old_precision = precisions.get(x);
			}
			for(Integer key:inverted_index.keySet()){//loop through inverted index hashmap and put precision values according to recall levels
				for(int p=recalls.size()-1; p>=0; p--){
					double recall_level = recalls.get(p);
					double precision_value = precisions.get(p);
					if(recall_level<=100 && recall_level>=key)//check recall levels from 100 to all the way 0, e.g 100 to 90 check if any precision value is present for this recall level if yes then save it to inve index hashmap
						inverted_index.put(key, precision_value);
				}
			}

			for(Integer key:inverted_index.keySet()){//put inverted index hashmap values into arraylist so that we can print them in any order
				PrecisionPojo precisionPojo = new PrecisionPojo(key, inverted_index.get(key));
				inverted_index_array.add(precisionPojo);
			}
			for(int v=inverted_index_array.size()-1; v>=0; v--){//print data of inverted index array list
				System.out.println("(" + df.format(inverted_index_array.get(v).getPrecisionValues()) + "," + inverted_index_array.get(v).getRecallLevels() + "%" + ")");
				output_file.write("(" + df.format(inverted_index_array.get(v).getPrecisionValues()) + "," + inverted_index_array.get(v).getRecallLevels() + "%" + ")");	
				output_file.write(System.getProperty("line.separator"));
			}
		}
		for(String key:all_average_precision.keySet()) {//loop through average precision for each query of engine
			double mean_average_precision = 0;
			int number_of_run =0;
			double total_precision=0;
			String[] engine_average_precision  = all_average_precision.get(key).split(",");//get average precision of engine queries
			for(int i=0; i<engine_average_precision.length; i++) {//loop through all average precision
				total_precision = total_precision + Double.parseDouble(engine_average_precision[i]);//add all average precision obtained from queries of a perticular engine
				number_of_run++;//cal how many time we have added average precision values of queries of a engine
			}
			mean_average_precision = total_precision / number_of_run;//calculate mean average precision for perticular engine
			System.out.println("\nMean average precision	for engine " + key + "=" + df.format(mean_average_precision));//print mean average precision for each engine
			output_file.write("\nMean average precision	for engine " + key + "=" + df.format(mean_average_precision));//wite mean average precision for each engine
		}
		output_file.close();//close printwriter after writing in file so that it can flush old values and get ready for new values as it has limited capacity to hold data
	}
}
//create custom class for storing multiple values in arraylist by passing custom object
class PrecisionPojo {
	private int recall_levels;
	private double precision_values;

	PrecisionPojo(int recall_levels, double precision_values) {
		this.recall_levels = recall_levels;
		this.precision_values = precision_values;
	}

	Integer getRecallLevels() {
		return recall_levels;
	}

	Double getPrecisionValues() {
		return precision_values;
	}
}
