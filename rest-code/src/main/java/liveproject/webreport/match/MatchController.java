package liveproject.webreport.match;

import liveproject.webreport.season.Season; //added
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// commented out: import org.springframework.stereotype.Controller;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity; //added
import org.springframework.web.bind.annotation.*; //added

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class MatchController {

    /*
    GET	/season	get list of seasons available	200 (OK)
    GET	/season-report/{season}	get season statistics for {season}	200 (OK) or 404 (NOT FOUND)
    GET	/matches-report/{season}	get all match results for {season}	200 (OK) or 404 (NOT FOUND)
    POST	/match/{season}	add set of match results to {season}	201 (CREATED)
    GET	/v3/api-docs	shows the OpenAPI3 JSON structures	200 (OK)
     */

    private MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @RequestMapping(value="/season", method= RequestMethod.GET)
    public List<String> getSeasons(){
        List<String> result;
        result = matchService.getAllSeasons();
        if(result == null)
            result = new ArrayList<String>();
        return result;
    }

    @RequestMapping(value="/season-report/{season}", method= RequestMethod.GET)
    public ResponseEntity<Season> getSeasonReport(@PathVariable("season") String season ){
        Season result = matchService.aggregateSeason(season);
        if(result.getTeamResults() == null || result.getTeamResults().isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value="/match/{season}", method= RequestMethod.POST)
    public ResponseEntity<String> addMatches(@PathVariable("season") String season ,
                                             @RequestBody List<Match> matches){

        if(season == null || season.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Season not specified");
        } else {
            Pattern pattern = Pattern.compile("[\\d]{4}-[\\d]{4}");
            Matcher matcher = pattern.matcher(season);
            boolean matchFound = matcher.find();
            if(!matchFound){
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Season not correctly specified");
            }
        }
        Map<String, Integer> counts = matchService.saveAll(season, matches);

        Integer created = 0;
        if(counts != null && counts.get(season) != null){
            created = counts.get(season);
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(created.toString());
    }

    @RequestMapping(value="/matches-report/{season}", method= RequestMethod.GET)
    public ResponseEntity<Set<Match>> getMatchesForSeason(@PathVariable String season ){
        Set<Match> result = matchService.getAllBySeasonSorted(season);
        if(result == null || result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(result);
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result);
    }

}
