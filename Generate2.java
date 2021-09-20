import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
class Generate2 {
	public static void main(String[] args) throws Exception {

        // Using Scanner for Getting Input from User
        Scanner input = new Scanner(System.in);

		System.out.println("How many engine you want to generate(1-26)");
        int engines = input.nextInt();
        System.out.println("You want engines "+engines);

		File results_file = new File("results.txt");
		PrintWriter output_file = (new PrintWriter(new FileWriter(results_file)));
		File engine_file = new File("engine.txt");
		PrintWriter output_engine_file = (new PrintWriter(new FileWriter(engine_file)));

		Random random = new Random();
		String[] engines_names = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		for(int i=0; i<1500; i++){
			for(int j=0; j<engines; j++){
				int random_doc = random.nextInt(1500);
				int random_score = random.nextInt(1000);
				if(j<engines-1){
				String output = engines_names[j] + ";" + random_doc + ";" + random_score + "\t";
				output_file.write(output);
				System.out.print(output);
				}
				else {
				String output = engines_names[j] + ";" + random_doc + ";" + random_score;
				output_file.write(output);
				System.out.print(output);
				}

			}
			System.out.println();
			if(i<1500-1)
			output_file.write(System.getProperty("line.separator"));
		}
		output_file.close();
		for(int e=0; e<engines; e++){
			double engine_weight = Math.round((1+random.nextDouble())*100.0)/100.0;
			if(e<engines-1){
				String weight = engines_names[e] + ";" + engine_weight +"\t";
				output_engine_file.write(weight);
			}
			else{
				String weight = engines_names[e] + ";" + engine_weight;
				output_engine_file.write(weight);
			}
		}
		output_engine_file.close();
		System.out.println();
		System.out.println("result file : results.txt");
		System.out.println("engine file :  engine.txt");
	}
}
