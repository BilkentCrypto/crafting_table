// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.4;

import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Burnable.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Supply.sol";

contract CraftingTable is ERC1155, ERC1155Burnable, ERC1155Supply {
    address owner;


    struct Item {
        uint8 id;
        uint8 amount;
    }

    struct Recipe {
        mapping( uint8 => Item ) requiredItems;
        uint8 amountToProduce;
        bool isCraftableWithoutCraftingTable;
    }


    mapping( uint8 => Recipe) public recipes;
    //0.0103
    constructor() ERC1155("") {
        owner = msg.sender;
        
        recipes[0].requiredItems[0] = ( Item( 2, 4) );
        recipes[0].amountToProduce = 1;

        recipes[2].requiredItems[0] = ( Item( 1, 1) );
        recipes[2].amountToProduce = 4;

        recipes[3].requiredItems[0] = ( Item( 2, 2) );
        recipes[3].amountToProduce = 4;

        recipes[4].requiredItems[0] = ( Item( 3, 1) );
        recipes[4].requiredItems[1] = ( Item( 10, 1) );
        recipes[4].amountToProduce = 4;

        recipes[5].requiredItems[0] = ( Item( 13, 8) );
        recipes[5].amountToProduce = 1;

        recipes[6].requiredItems[0] = ( Item( 2, 8) );
        recipes[6].amountToProduce = 1;
        
        recipes[9].requiredItems[0] = ( Item( 2, 6) );
        recipes[9].requiredItems[1] = ( Item( 3, 1) );
        recipes[9].amountToProduce = 3;

        recipes[11].requiredItems[0] = ( Item( 17, 8) );
        recipes[11].requiredItems[1] = ( Item( 24, 1) );
        recipes[11].amountToProduce = 1;

        recipes[19].requiredItems[0] = ( Item( 3, 2) );
        recipes[19].requiredItems[1] = ( Item( 2, 3) );
        recipes[19].amountToProduce = 1;

        recipes[20].requiredItems[0] = ( Item( 3, 2) );
        recipes[20].requiredItems[1] = ( Item( 2, 3) );
        recipes[20].amountToProduce = 1;

        




    }

    function constantTest() external {

        

        recipes[0].requiredItems[0] = ( Item( 2, 4) );
        recipes[0].amountToProduce = 1;

        recipes[2].requiredItems[0] = ( Item( 1, 1) );
        recipes[2].amountToProduce = 4;

        recipes[3].requiredItems[0] = ( Item( 2, 2) );
        recipes[3].amountToProduce = 4;

        recipes[4].requiredItems[0] = ( Item( 3, 1) );
        recipes[4].requiredItems[1] = ( Item( 10, 1) );
        recipes[4].amountToProduce = 4;

        recipes[5].requiredItems[0] = ( Item( 13, 8) );
        recipes[5].amountToProduce = 1;

        recipes[6].requiredItems[0] = ( Item( 2, 8) );
        recipes[6].amountToProduce = 1;
        
        recipes[9].requiredItems[0] = ( Item( 2, 6) );
        recipes[9].requiredItems[1] = ( Item( 3, 1) );
        recipes[9].amountToProduce = 3;

        recipes[11].requiredItems[0] = ( Item( 17, 8) );
        recipes[11].requiredItems[1] = ( Item( 24, 1) );
        recipes[11].amountToProduce = 1;

        recipes[19].requiredItems[0] = ( Item( 3, 2) );
        recipes[19].requiredItems[1] = ( Item( 2, 3) );
        recipes[19].amountToProduce = 1;

        recipes[20].requiredItems[0] = ( Item( 3, 2) );
        recipes[20].requiredItems[1] = ( Item( 2, 3) );
        recipes[20].amountToProduce = 1;
    }

    //function getRequiredItem( uint8 _recipeId, uint8 _ItemId ) external view returns( Item )

    
    modifier onlyOwner {
        require( msg.sender == owner );
        _;
    }

    function setURI(string calldata newuri) external onlyOwner {
        _setURI(newuri);
    }


    //contract functions

    function craftItemWithoutCraftingTable( uint8 _id ) external {
        
    }




    // The following functions are overrides required by Solidity.

    function _beforeTokenTransfer(address operator, address from, address to, uint256[] memory ids, uint256[] memory amounts, bytes memory data)
        internal
        override(ERC1155, ERC1155Supply)
    {
        super._beforeTokenTransfer(operator, from, to, ids, amounts, data);
    }
}