# given-when-then-js
basic given when then test helper [Javascript]

# how to use
```javascript
var given = require('given-when-then-js');
var expect = require('chai').expect;


it('runs in the most basic form', () => {
	given(1)
		.when(value => 2)
		.then(value => value == 2);
});

it('should do a basic flow', () => {
	var givenValue = 1;
	var whenValue = 2;

	given(givenValue, "given a basic value")
		.when(value => {
			expect(value).to.equal(givenValue);
			/*perform action and return result*/
			return whenValue;
		}, "given value should be passed to when")
		.then(value => {
			/*check if the result matches the expected result*/
			expect(value).to.equal(whenValue);
		}, "when value should be passed to then");
});
```
