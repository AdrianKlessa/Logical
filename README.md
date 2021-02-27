DNF / CNF generator for a logical formula

Topic:
4. Program that turns a given valid formula into a conjunctive/disjunctive normal form (using 0-1 tables).

   Outputs values for the formula as well


    1. How to run it

Download the .jar file, name it whatever or leave it as it is. Assuming it’s named “CNFDNF.jar”, open a console prompt, go to the directory where the file is located and type in “java -jar CNFDNF.jar” and press enter.



    2. How to use it

	Once the program’s running it will ask you to type in a formula. The program supports the following logical operators


Negation
!
(Just an exclamation mark)
Disjunction
v
(The lower-case latin letter “V”)
Conjunction
^
(The character often used to denote raising a number to a power, usually typed in with SHIFT+6)
Implication
->
(Hyphen followed by a “greater than” symbol)
Equivalence
<->
(“Smaller than”,hyphen,”greater then”)

When providing a formula do not use spaces or “v” as a variable. All other latin characters are allowed and interpreted as variable names. The program supports brackets (only the round ones, but “nested” levels can be used). The following examples are all valid formulas:

    • (a<->b)->((a->b)^(b->a))
    • a->b
    • (avb)v(b^c)
    • (a->b)
    • ava
    • (a)va
    • ((a->b)^(!bv!a))

    3. How it works

Evaluation

There is an abstract class that is called “Item” and it functions like a binary tree. It has something in the middle (a variable or a function), an item on the lower left and an item on the lower right. We can evaluate an item to get its true/false value, when doing so on the top of the tree we get the evaluation of an entire logical formula. However, to do so, we need to evaluate the lower parts of the tree and use their values. Therefore, the algorithm is recursive.

A variable is an item. To evaluate it, we just return the value it holds (true or false). It also has a name (usually a single letter like p or q in the logical formulas). The negation is special in that it only has a “left” lower part and no right part (the right part is always null).

A logical operation (function) is an item. To evaluate it, we first evaluate the left and right side, and depending on the function we do something else with it.

The algorithm is basically going through a binary tree.

Finding the variables in an expression

We just go through each character in the user-input string and look for latin characters that are not “v” (since that one is interpretted as an operator). We add them to a list, making sure that there are no duplicates.

Creating rows of possible evaluations of the formula

We create rows of our table. Each rows contains a list of variables along with their assigned values (true or false) and a final output – the value of the function under this evaluation, currently unknown (so null).

Just like on paper, we make sure that we used all possible evaluations by starting with all variables as false, then the first one as true and all other as false etc., which corresponds to counting from 0 in binary.


Understanding a user-input formula

stringToTree(String expression, Row rowBeingEvaluated) function. It returns an Item (that is, a tree)

We need to make sure that the formula is not empty. Then we find the first operand (preferably not negation) that isn’t in brackets (this is the main operator). If everything is located inside brackets, then we remove them.

After finding the main operand we create a tree with that operator (or variable if the whole formula is actually just a single variable) as a root. We cut the string into everything that was on the left of that operator and everything that was on the right. Then we to the left side we assign stringToTree(leftPartOfString,  rowBeingEvaluated) and to the right stringToTree(rightPartOfString,  rowBeingEvaluated). This way we go down the tree, and in turn with each function invocation we get smaller and smaller expressions – it’s treated like an onion, with us removing the layers one after another.

When the function encounters a variable we know that we are at the end of this branch of the tree. We add the variable and assign it the value indicated in the row that we passed to the function.

Getting the final value of the expression

Then we only need to invoke .evaluate() on the top item of the tree and we’ve got the value of the formula under that evaluation. We can use this value, as well as knowing what the values of each variable were to generate an CNF or DNF form of the expression, just as we do on paper. 

For DNF, we look for rows where the final output was 1, and for each of these rows we put the values of the variables, connected by the conjunction operation. Such a string is put into brackets, and brackets (corresponding to each row) are connected with disjunction 

For CNF, we look for rows where the final output was 0, and for each of these rows we put the negated values of the variables, connected by the disjunction operation. Such a string is put into brackets, and brackets (corresponding to each row) are connected with conjunction.

If none of the evaluations was 1, then the expression is an anti-tautology (always false), so the DNF is just (p^!p)

If none of the evaluations was 0, then the expression is a tautology (always true), so the CNF is just (pv!p) 
