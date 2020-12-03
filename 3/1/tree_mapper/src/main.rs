use std::fs::File;
use std::io::{self, BufRead};
use std::path::Path;

fn main() {
    let mut line_vec: Vec<Vec<char>> = Vec::new();
    if let Ok(lines) = read_lines("./src/input.txt") {
        for line in lines {
            if let Ok(l) = line {
                //println!("{}", l);
                let characters: Vec<char> = l.chars().collect();
                line_vec.push(characters);
            }
        }
    }

    println!("Number of lines: {}", line_vec.len());

    let number_of_trees = count_trees(&line_vec,1,1);
    let number_of_trees2 = count_trees(&line_vec,1,3);
    let number_of_trees3 = count_trees(&line_vec,1,5);
    let number_of_trees4 = count_trees(&line_vec,1,7);
    let number_of_trees5 = count_trees(&line_vec,2,1);

    let answer2 = number_of_trees * number_of_trees2 * number_of_trees3 * number_of_trees4 * number_of_trees5;

    println!("Answer: {}", number_of_trees2);
    println!("Answer 2: {}", answer2);
}

fn count_trees(map: &Vec<Vec<char>>, down_step: usize, right_step: usize) -> u64 {
    let line_length = map[0].len();
    let mut current_pos = 0;
    let mut number_of_trees = 0;
    for (i, positions) in map.iter().enumerate() {
        if i % down_step == 0 {
            let rel_position = current_pos % line_length;
            let symbol = positions[rel_position];
            //println!("{} : {}", i, symbol);
            if symbol == '#' {
                number_of_trees += 1;
            }
            current_pos += right_step;
        }
    }
    number_of_trees
}

fn read_lines<P>(filename: P) -> io::Result<io::Lines<io::BufReader<File>>>
where P: AsRef<Path>, {
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}
