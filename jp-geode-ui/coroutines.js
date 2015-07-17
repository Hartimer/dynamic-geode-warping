// Just having fun with ES6
function* _coworker() {
    console.log('coworker starts...');
    var a = yield 1;
    console.log('coworker', a);
    var b = yield 3;
    console.log('coworker', b);
}

function worker() {
    var coworker = _coworker();
    console.log('worker starts...')
    var c = coworker.next();    
    console.log('worker', c.value);
    var d = coworker.next(2);
    console.log('worker', d.value);
    coworker.next(4);
}

worker();

