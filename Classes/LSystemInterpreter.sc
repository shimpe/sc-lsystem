LSystemInterpreter {
    var <>lsystem;
    var <>actions;
    var <>globalstate;

    *new {
        | lsystem = nil, actions = nil, globalstate = nil|
        ^super.new.init(lsystem, actions, globalstate);
    }

    init {
        | lsystem = nil, actions = nil, globalstate = nil |
        this.lsystem = lsystem;
        if (actions.isNil) {
            actions = ();
        };
        this.actions = actions;
        this.globalstate = globalstate;
    }

    setLSystem {
        | lsys |
        this.lsystem = lsys;
        ^this;
    }

    lSystem {
        ^this.lsystem;
    }

    setActions {
        | actions |
        this.actions = actions;
        ^this;
    }

    addAction {
        | symbol, action, stateobject=nil |
        this.actions[symbol.asSymbol] = [action, stateobject];
        ^this;
    }

    action {
        | symbol |
        ^this.actions[symbol.asSymbol][0];
    }

    setGlobalState {
        | globalstate |
        this.globalstate = globalstate;
        ^this;
    }

    globalState {
        ^this.globalstate;
    }

    updateAction {
        | symbol, action |
        if (this.actions[symbol.asSymbol].isNil) {
            this.actions[symbol.asSymbol] = [nil, nil]
        };
        this.actions[symbol.asSymbol][0] = action;
        ^this;
    }

    updateState {
        | symbol, state |
        if (this.actions[symbol.asSymbol].isNil) {
            this.actions[symbol.asSymbol] = [nil, nil]
        };
        this.actions[symbol.asSymbol][1] = state;
        ^this;
    }

    state {
        | symbol |
        ^this.actions[symbol.asSymbol][1];
    }

    size {
        ^this.lsystem.getCalculatedString.size;
    }

    step {
        // evaluate single step
        | i |
        var str = this.lsystem.getCalculatedString ?? "";
        var st = str[i] ?? "";
        if (this.lsystem.isConstant(st).not) {
            var symbol = st.asSymbol;
            if (this.actions[symbol].notNil) {
                var oldstateobject = this.state(symbol);
                var gs = this.globalstate;
                if (oldstateobject.notNil || gs.notNil) {
                    // action with stateobject: call the action and update the stateobject
                    var newstateobject = this.action(symbol).value(gs, oldstateobject);
                    this.updateState(symbol, newstateobject[1]);
                    this.globalstate = newstateobject[0];
                } {
                    // action without stateobject: just call the action
                    this.action(symbol).value();
                };
            };
        };
        ^this;
    }

    stepRange {
        // evaluate range of steps
        | from, to |
        var str = this.lsystem.getCalculatedString ?? "";
        (from..to).do({
            | st |
            this.step(st);
        });
        ^this;
    }

    run {
        // evaluate all steps
        var str = this.lsystem.getCalculatedString ?? "";
        this.stepRange(0, str.size);
        ^this;
    }
}