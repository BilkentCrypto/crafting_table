// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.4;

import "@openzeppelin/contracts/token/ERC1155/ERC1155.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Burnable.sol";
import "@openzeppelin/contracts/token/ERC1155/extensions/ERC1155Supply.sol";
import "hardhat/console.sol";


contract CraftingTableV2 is ERC1155, ERC1155Burnable, ERC1155Supply {
    address owner;


    //Recipe data

    /*
        first byte [isCraftable(bool)(1 bit), isCraftableWithoutCraftingTable(bool)(1bit), amountToProduce(4 bit)(with value of minus 1 so its range is not 0-15 it is 1-16), itemNumber(2bit)]
        next 3 bytes are Item( id(5 bit), amount(3 bit)(minus 1, 1-9 range)]  

        Part 1: 0xc113000000000000cd080000cd110000ce501800816f00008117000000000000
        Part 2: 0x000000008a1518000000000082c08f0000000000000000000000000000000000
        Part 3: 0x0000000000000000000000008219120082196a0082198a008219820082197a00
    */

    bytes32 constant recipes1 = 0xc113000000000000cd080000cd110000ce501800816f00008117000000000000;
    bytes32 constant recipes2 = 0x000000008a1518000000000082c08f0000000000000000000000000000000000;
    bytes32 constant recipes3 = 0x0000000000000000000000008219120082196a0082198a008219820082197a00;
    bytes4 constant TRUE_BYTE = 0x00000001;

    string constant contractURILink = "https://raw.githubusercontent.com/BilkentCrypto/crafting_table/Item-Metadatas/ItemData/Metadatas/Contract_Metadata.json";

    struct Item {
        uint8 id;
        uint8 amount;
    }

    struct Recipe {
        bool isCraftable;
        bool isCraftableWithoutCraftingTable;
        uint8 amountToProduce;
        uint8 itemNumber;
        Item[3] requiredItems;
    }


    function contractURI() public view returns (string memory) {
        return contractURILink;
    }


    //0.0079               
    constructor() ERC1155("https://raw.githubusercontent.com/BilkentCrypto/crafting_table/Item-Metadatas/ItemData/Metadatas/itemMetadatasV2/{id}.json") {
        owner = msg.sender;
    
    }

    modifier onlyOwner {
        require( msg.sender == owner );
        _;
    }

    function setURI(string calldata newuri) external onlyOwner {
        _setURI(newuri);
    }

    function mint( uint id, uint amount ) external onlyOwner {
        _mint( owner, id, amount, "" );
    }

    function mintTest( uint startIndex, uint endIndex, uint amount ) external onlyOwner {
        for( uint i = startIndex; i <= endIndex; i++ ) {
                _mint( owner, i, amount, "" );
        }
    }

    function getInteger( bytes4 recipeByte, uint8 startIndex, uint8 length ) internal pure returns( uint8 ) {
        return uint8( bytes1( recipeByte << startIndex >> (8 - length) ) );
    }

    function getRecipe( uint8 index ) internal pure returns( Recipe memory ) {
        Recipe memory recipe;
        bytes4 recipeByte = getRecipeByte( index );

        recipe.isCraftable = ( TRUE_BYTE == (recipeByte >> 31));
        recipe.isCraftableWithoutCraftingTable = ( TRUE_BYTE == (recipeByte << 1 >> 31));
        recipe.amountToProduce = getInteger( recipeByte, 2, 4 ) + 1;

        uint8 itemNumber = getInteger( recipeByte, 6, 2 );
        recipe.itemNumber = itemNumber;

        for(uint8 i = 0; i < itemNumber; i++) {
            uint8 startIndex = 8 * (i + 1);
            recipe.requiredItems[i] = Item( getInteger( recipeByte, startIndex, 5 ), getInteger( recipeByte, startIndex + 5, 3 ) + 1 );
        }

        return recipe;
    }

    function viewRecipe( uint8 index ) external pure returns( Recipe memory ) {
        return getRecipe( index );
    }
    
    
    function getRecipeByte( uint8 index ) internal pure returns( bytes4 ) {
        bytes32 toUseData;
        if( index < 8 ) {
            toUseData = recipes1;
        } else if( index < 16 ) {
            index -= 8;
            toUseData = recipes2;
        } else {
            index -= 16;
            toUseData = recipes3;
        }
        return bytes4( toUseData << index * 32 ); // 8 for bit * 4 for bytes = 32
    }
    //0.00006902 (very cheap)
 
    //produced item number will be recipe.producedItem * repeat
    function craft( uint8 id, uint repeat ) external {
        Recipe memory recipe = getRecipe( id );
        address msgSender = msg.sender;

        require( recipe.isCraftable == true ); //it should be craftable
        require( repeat > 0 ); // bigger than zero amount can be crafted

        if( recipe.isCraftableWithoutCraftingTable == false ) {
            require( balanceOf( msgSender, 0 ) > 0 ); //user should have crafting table to craft 3x3 recipes    
        }

        for( uint8 i = 0; i < recipe.itemNumber; i++ ) {
            Item memory currentItem = recipe.requiredItems[i];
            //require( balanceOf( msgSender, currentItem.id ) >= currentItem.amount * repeat ); //not enough required items
            _burn( msgSender, currentItem.id, currentItem.amount * repeat );
        }

        _mint( msgSender, id, recipe.amountToProduce * repeat, "" );




    } 

    
    // The following functions are overrides required by Solidity.

    function _beforeTokenTransfer(address operator, address from, address to, uint256[] memory ids, uint256[] memory amounts, bytes memory data)
        internal
        override(ERC1155, ERC1155Supply)
    {
        super._beforeTokenTransfer(operator, from, to, ids, amounts, data);
    }
}