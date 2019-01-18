CalculateTester : UnitTest {
    test_default {
        var p = LSystem.new;
        this.assertEquals(p.axiom(), "");
        this.assertEquals(p.iterations(), 0);
        this.assertEquals(p.constants().as(Array), Set[].as(Array));
        this.assertEquals(p.rules(), ());
        this.assertEquals(p.getCalculatedString(), "");
    }

    test_constructor {
        var p = LSystem.new(iterations:3);
        var q = LSystem.new(axiom:"x");
        var r = LSystem.new(constants:Set[\a, \b], rules:(\a : "bbb"));
        this.assertEquals(p.iterations(), 3);
        this.assertEquals(q.axiom(), "x");
        this.assertEquals(r.constants().as(Array), Set[\a, \b].as(Array));
        this.assertEquals(r.rules(), (\a : "bbb"));
    }

    test_setter {
        var p = LSystem.new(iterations:5, axiom:"X+Y+X", constants:Set[\Y], rules:(\X : "[Y^X^Y]"));
        this.assertEquals(p.iterations(), 5);
        this.assertEquals(p.axiom(), "X+Y+X");
        this.assertEquals(p.constants().as(Array), Set[\Y].as(Array));
        this.assertEquals(p.rules(), (\X : "[Y^X^Y]"));
        p.setNoOfIterations(4);
        p.setAxiom("XXX");
        p.addConstant('t');
        p.addRule(\Y, "XXX");
        this.assertEquals(p.iterations(), 4);
        this.assertEquals(p.axiom(), "XXX");
        this.assertEquals(p.constants().as(Array), Set[\Y, 't'].as(Array));
        this.assertEquals(p.rules(), (\X : "[Y^X^Y]", \Y : "XXX"));
    }

    test_calculate {
        var p = LSystem.new(iterations:1, axiom:"X+Y+X", constants:Set[\Y], rules:(\X : "[Y^X^Y]"));
        this.assertEquals(p.getCalculatedString(), "[Y^X^Y]+Y+[Y^X^Y]");
        p.setNoOfIterations(2);
        this.assertEquals(p.getCalculatedString(), "[Y^[Y^X^Y]^Y]+Y+[Y^[Y^X^Y]^Y]");
        this.assertEquals(p.length(), 29);
        p.setNoOfIterations(1);
        this.assertEquals(p.length(), 17);
        this.assertEquals(p.getCalculatedString(), "[Y^X^Y]+Y+[Y^X^Y]");
    }
}

LSystemTester {
    *new {
        ^super.new.init();
    }

    init {
        CalculateTester.run;
    }
}