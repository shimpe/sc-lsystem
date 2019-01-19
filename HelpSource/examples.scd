(
var win = Window("Example Graphical LSystem", bounds:Rect(0,0,1000,700));
var view = UserView(win, win.view.bounds.insetBy(50,50));
var lsys = LSystem(
    iterations:12,
    axiom:"FX",
    constants:Set[],
    rules:(\X : "X+YF+",
           \Y : "-FX-Y"));
var interp = LSystemInterpreter(
    lsystem:lsys,
    actions:(
        'F' : [{
            | globalstate, symbolstate |
            globalstate[\pos] = globalstate[\pos] + (globalstate[\dir]*globalstate[\len]);
            Pen.lineTo(globalstate[\pos]);
            [globalstate, symbolstate];
        }, nil],
        'X' : [{
            | globalstate, symbolstate |
            globalstate[\pos] = globalstate[\pos] + (globalstate[\dir]*globalstate[\len]);
            Pen.lineTo(globalstate[\pos]);
            [globalstate, symbolstate];
        }, nil],
        'Y' : [{
            | globalstate, symbolstate |
            globalstate[\pos] = globalstate[\pos] + (globalstate[\dir]*globalstate[\len]);
            Pen.lineTo(globalstate[\pos]);
            [globalstate, symbolstate];
        }, nil],
        '-' : [{| globalstate, symbolstate |
            globalstate[\dir] = (globalstate[\dir].rotate(globalstate[\angle].degrad));
            [globalstate, symbolstate];
        }, nil],
        '+' : [{| globalstate, symbolstate |
            globalstate[\dir] = (globalstate[\dir].rotate(globalstate[\angle].neg.degrad));
            [globalstate, symbolstate];
        }, nil]
    )
);

view.resize = 5;
view.background_(Color.white);
view.drawFunc_({
    |userview|
    Pen.use {
        interp.setGlobalState( globalstate:(
            \dir : 0@1.neg,
            \pos : (win.view.bounds.width/2)@(win.view.bounds.height/5),
            \len : 3,
            \angle:90 ) );
        Pen.width = 2;
        Pen.strokeColor_(Color.black);
        Pen.fillColor_(Color.black);
        Pen.moveTo(interp.globalstate[\pos]);
        interp.run();
        Pen.stroke;
    };
});
win.front;
)
