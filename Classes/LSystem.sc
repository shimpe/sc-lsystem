LSystem {
    var <>system;
    var <>calculatedString;
    var <>cacheValid;
    var <>noOfIterations;

    *new {
        | iterations = 0, axiom = nil, constants=nil, rules = nil|
        ^super.new.init(iterations, axiom, constants, rules);
    }

    init {
        | iterations = nil, axiom = nil, constants = nil, rules = nil |
        this.system = ();
        this.calculatedString = "";
        this.cacheValid = false;
        this.system[\iterations] = iterations ?? 0;
        this.system[\axiom] = axiom ?? "";
        this.system[\constants] = constants ?? Set[];
        this.system[\rules] = rules ?? ();
    }

    setNoOfIterations {
        | i |
        if (this.system[\iterations] != i) {
            this.cacheValid = false;
            this.system[\iterations] = i;
        }
        ^this;
    }

    setAxiom {
        | axiom |
        if (this.system[\axiom].notNil) {
            if (this.system[\axiom].compare(axiom, ignoreCase:true) != 0) {
                this.cacheValid = false;
                this.system[\axiom] = axiom;
            }
        }
        ^this;
    }

    addConstant {
        | c |
        if (this.system[\constants].includes(c).not) {
            this.cacheValid = false;
            this.system[\constants].add(c);
        }
        ^this;
    }

    addRule {
        | from, to |
        this.cacheValid = false;
        this.system[\rules][from] = to;
        ^this;
    }

    iterations {
        ^this.system[\iterations];
    }

    axiom {
        ^this.system[\axiom];
    }

    constants {
        ^this.system[\constants];
    }

    rules {
        ^this.system[\rules];
    }

    isConstant {
        | c |
        ^this.system[\constants].includes(c);
    }

    getCalculatedString {
        if (this.cacheValid.not) {
            this.pr_recalculate();
        }
        ^this.calculatedString;
    }

    length {
        if (this.cacheValid.not) {
            this.pr_recalculate();
        }
        ^this.calculatedString.size;
    }

    pr_recalculate {
        this.calculatedString = this.system[\axiom];
        this.iterations().do({
            | i |
            var newString = "";
            //i.debug("iteration");
            //this.calculatedString.debug("currently calculated string");
            this.calculatedString.do
            ({
                | c |
                var ruleFound = false;
                //this.calculatedString.debug("str");
                //c.debug("character");
                this.system[\rules].pairsDo
                ({
                    | key, value |
                    //("does rule"+key+" match character"+c+" ?").postln;
                    if (ruleFound.not && (c.asSymbol == key.asSymbol))
                    {
                        //"***MATCH***".postln;
                        newString = newString ++ value;
                        //newString.debug("new string 1");
                        ruleFound = true;
                    };
                });
                if (ruleFound.not)
                {
                    //("no ***rules*** applicable for character" + c).postln;
                    newString = newString ++ c;
                    //lnewString.debug("new string 2");
                };
            });
            this.calculatedString = newString;
        });
        this.cacheValid = true;
        ^this;
    }
}
