RunTester : UnitTest {
    test_run_without_internal_state {
        var lsys = LSystem(2, "X", nil, (\X : "Y-X-Y", \Y : "Z" ));
        var str = lsys.getCalculatedString; // Z-Y-X-Y-Z
        var interp = LSystemInterpreter(lsystem:lsys);
        ~counter = 0;
        interp.addAction(\Z, { ~counter = ~counter + 1});
        interp.run();
        this.assertEquals(~counter, 2);
    }

    test_run_with_internal_state {
        var lsys = LSystem(2, "X", nil, (\X : "Y-X-Y", \Y : "Z" ));
        var str = lsys.getCalculatedString;
        var interp = LSystemInterpreter(lsystem:lsys);
        interp.addAction(\Z, { |globalstate, state| state[\counter] = state[\counter] + 1; [globalstate, state]; }, (\counter : 0));
        interp.run();
        this.assertEquals(interp.state(\Z)[\counter], 2);
    }

    test_step_with_internal_state {
        var lsys = LSystem(2, "X", nil, (\X : "Y-X-Y", \Y : "Z" )); // Z-Y-X-Y-Z
        var str = lsys.getCalculatedString;
        var interp = LSystemInterpreter(lsystem:lsys);
        interp.addAction(\Z, { |globalstate, state|
            state[\counter] = state[\counter] + 1;
            [globalstate, state];
        }, (\counter : 0));
        7.do ({
            | i |
            interp.step(i);
            this.assertEquals(interp.state(\Z)[\counter], 1);
        });
        interp.step(8);
        this.assertEquals(interp.state(\Z)[\counter], 2);
    }
}

LSystemInterpreterTester {
    *new {
        ^super.new.init();
    }

    init {
        RunTester.run;
    }
}