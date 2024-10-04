// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.26;
import "remix_tests.sol"; // импорт тестовой библиотеки
import "../contracts/SecurityToken.sol";
contract SecurityTokenTest {
    SecurityToken token;
    
    function beforeAll() public {
        token = new SecurityToken(1000); // Создаем контракт с начальными токенами
    }

    function checkInitialSupply() public {
        Assert.equal(token.totalSupply(), 1000 * 10 ** 18, "Initial supply is incorrect");
    }

    function checkTransfer() public {
        bool result = token.transfer(address(0x1), 100);
        Assert.ok(result, "Transfer failed");
        Assert.equal(token.balanceOf(address(0x1)), 100, "Balance incorrect after transfer");
    }
}
