RunTester : UnitTest {
    test_run_without_internal_state {
        var lsys = LSystem(2, "X", nil, (\X : "Y-X-Y", \Y : "Z" ));
        var str = lsys.getCalculatedString; // XY-X-YZY--XY-X-Y--ZY
        var interp = LSystemInterpreter(lsystem:lsys);
        ~counter = 0;
        interp.addAction(\Z, { ~counter = ~counter + 1});
        interp.run();
        this.assertEquals(~counter, 2);
    }

    test_run_with_internal_state {
        var lsys = LSystem(2, "X", nil, (\X : "Y-X-Y", \Y : "Z" ));
        var str = lsys.getCalculatedString; // XY-X-YZY--XY-X-Y--ZY
        var interp = LSystemInterpreter(lsystem:lsys);
        interp.addAction(\Z, { |state| state[\counter] = state[\counter] + 1; state; }, (\counter : 0));
        interp.run();
        this.assertEquals(interp.state(\Z)[\counter], 2);
    }

    test_step_with_internal_state {
        var lsys = LSystem(2, "X", nil, (\X : "Y-X-Y", \Y : "Z" ));
        var str = lsys.getCalculatedString; // XY-X-YZY--XY-X-Y--ZY
        var interp = LSystemInterpreter(lsystem:lsys);
        interp.addAction(\Z, { |state| state[\counter] = state[\counter] + 1; state; }, (\counter : 0));
        6.do ({
            | i |
            interp.step(i);
            this.assertEquals(interp.state(\Z)[\counter], 0);
        });
        interp.step(6);
        this.assertEquals(interp.state(\Z)[\counter], 1);
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