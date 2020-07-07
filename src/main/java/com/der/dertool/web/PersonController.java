package com.der.dertool.web;

import com.der.dertool.constants.DerResponse;
import com.der.dertool.vo.PersonVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-06 11:37
 */

/**
 * swagger演示controller
 */
@RestController
@RequestMapping("/v2/persons/")
@Api(description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Persons.")
public class PersonController {

    private static final List<PersonVo> persons = Lists.newArrayList(
            new PersonVo(1, "第一个用户", "第一个用户", 11),
            new PersonVo(2, "第二个用户", "第二个用户", 12),
            new PersonVo(3, "第三个用户", "第三个用户", 13)
    );

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Returns list of all Persons in the system.")
    public DerResponse<List<PersonVo>> getAllPersons() {
        return DerResponse.openSuccess(persons);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}", produces = "application/json")
    @ApiOperation("Returns a specific person by their identifier. 404 if does not exist.")
    public DerResponse<PersonVo> getPersonById(@ApiParam("Id of the person to be obtained. Cannot be empty.") @PathVariable int id) {
        return DerResponse.openSuccess(persons.get(id));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    @ApiOperation("Deletes a person from the system. 404 if the person's identifier is not found.")
    public DerResponse deletePerson(@ApiParam("Id of the person to be deleted. Cannot be empty.") @PathVariable int id) {
        persons.remove(id);
        return DerResponse.openSuccess();
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ApiOperation("Creates a new person.")
    public DerResponse createPerson(@ApiParam("Person information for a new person to be created.") @RequestBody PersonVo person) {
        persons.add(person);
        return DerResponse.openSuccess();
    }
}
