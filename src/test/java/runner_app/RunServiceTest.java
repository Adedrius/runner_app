package runner_app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.time.Duration;
import java.time.LocalDate;

class RunServiceTest {

    @Test
    void testGenerateRunIdWhenArchiveIsEmpty() {

        RunArchive archive = new RunArchive() {

            @Override
            public List<Run> getAllRecordedRuns() {
                return new ArrayList<>();
            }
            

            @Override
            public void addRun(Run run) {
            	
            }

            
            @Override
            public Optional<Run> getRun(int runId) {
                return Optional.empty();
            }
            

            @Override
            public boolean deleteRun(int runId) {
                return false;
            }
            
        };

        RunService service = new RunService(archive);

        assertEquals(1, service.generateRunId());
    }
    

	@Test
	void testGenerateRunIdWithExistingRuns() {
	
	    RunArchive archive = new RunArchive() {
	
	        @Override
	        public List<Run> getAllRecordedRuns() {
	        	
	            return List.of(
	            		
	                new EasyRun(1, 5.0, Duration.ofMinutes(30), 140,LocalDate.of(2026, 10, 1), 3),
	                new EasyRun(3, 7.0, Duration.ofMinutes(42), 145,LocalDate.of(2026, 10, 2), 4),
	                new EasyRun(5, 10.0, Duration.ofMinutes(60), 150,LocalDate.of(2026, 10, 3), 5)
	                
	            );
	        }
	
	        
	        @Override
	        public void addRun(Run run) {}
	
	        
	        @Override
	        public Optional<Run> getRun(int runId) {
	            return Optional.empty();
	        }
	
	        
	        @Override
	        public boolean deleteRun(int runId) {
	            return false;
	        }
	        
	    };
	
	    RunService service = new RunService(archive);
	
	    assertEquals(6, service.generateRunId());
	}
	
	
	

	@Test
	void testSearchRunByDateWithNoMatches() {
	
	    LocalDate searchDate = LocalDate.of(2026, 10, 5);
	
	    RunArchive archive = new RunArchive() {
	
	        @Override
	        public List<Run> getAllRecordedRuns() {
	        	
	            return List.of(	
	                new EasyRun(1, 5.0, Duration.ofMinutes(30), 140,LocalDate.of(2026, 10, 1), 3),
	                new EasyRun(2, 7.0, Duration.ofMinutes(42), 145,LocalDate.of(2026, 10, 2), 4)
	            );
	            
	        }
	
	        
	        @Override
	        public void addRun(Run run) {
	        	
	        }
	        
	
	        @Override
	        public Optional<Run> getRun(int runId) {
	            return Optional.empty();
	        }
	
	        @Override
	        public boolean deleteRun(int runId) {
	            return false;
	        }
	        
	    };
	
	    RunService service = new RunService(archive);
	    
	    List<Run> results = service.searchRunByDate(searchDate);
	
	    assertTrue(results.isEmpty());
	}

	
	
	@Test
	void testSearchRunByDateWithMultipleMatches() {
	
		LocalDate searchDate = LocalDate.of(2026, 10, 1);
	
	    RunArchive archive = new RunArchive() {
	
	        @Override
	        public List<Run> getAllRecordedRuns() {
	        	
	            return List.of(
	            		
	                new EasyRun(1, 5.0, Duration.ofMinutes(30), 140, LocalDate.of(2026, 10, 1), 3),
	                new EasyRun(2, 7.0, Duration.ofMinutes(42), 145, LocalDate.of(2026, 10, 1), 4)
	                
	            );
	            
	        }
	
	        
	        @Override
	        public void addRun(Run run) {
	        	
	        }
	        
	
	        @Override
	        public Optional<Run> getRun(int runId) {
	            return Optional.empty();
	        }
	
	        
	        @Override
	        public boolean deleteRun(int runId) {
	            return false;
	        }
	        
	    };
	
	    RunService service = new RunService(archive);
	    List<Run> results = service.searchRunByDate(searchDate);
	
	    assertEquals(2, results.size());
	    assertEquals(1, results.get(0).getRunId());
	    assertEquals(2, results.get(1).getRunId());
	}
    
	
	@Test
	void testSearchRunByDateWithOneMatch() {
	
		LocalDate searchDate = LocalDate.of(2026, 10, 1);
	
	    RunArchive archive = new RunArchive() {
	
	        @Override
	        public List<Run> getAllRecordedRuns() {
	        	
	            return List.of(
	            		
	                new EasyRun(1, 5.0, Duration.ofMinutes(30), 140,LocalDate.of(2026, 10, 1), 3),
	                new EasyRun(2, 7.0, Duration.ofMinutes(42), 145,LocalDate.of(2026, 10, 2), 4)
	                
	            );
	            
	        }
	
	        @Override
	        public void addRun(Run run) {
	        	
	        }
	        
	
	        @Override
	        public Optional<Run> getRun(int runId) {
	            return Optional.empty();
	        }
	
	        
	        @Override
	        public boolean deleteRun(int runId) {
	            return false;
	        }
	        
	    };
	
	    RunService service = new RunService(archive);
	    List<Run> results = service.searchRunByDate(searchDate);
	
	    assertEquals(1, results.size());
	    assertEquals(1, results.get(0).getRunId());
	}
	

	
    @Test
    void testSearchRunByDateWithEmptyArchive() {
        LocalDate searchDate = LocalDate.of(2026, 10, 1);

        RunArchive archive = new RunArchive() {
        	
            @Override
            public List<Run> getAllRecordedRuns() {        	
                return List.of();
            }

            @Override
            public void addRun(Run run) {
            	
            }

            @Override
            public Optional<Run> getRun(int runId) {
                return Optional.empty();
            }

            @Override
            public boolean deleteRun(int runId) {
                return false;
            }
        };

        
        RunService service = new RunService(archive);
        List<Run> results = service.searchRunByDate(searchDate);

        assertTrue(results.isEmpty());
        
    }

    
    @Test
    void testSearchRunByDateWithAllMatches() {
    	
        LocalDate searchDate = LocalDate.of(2026, 10, 1);

        RunArchive archive = new RunArchive() {
        	
            @Override
            public List<Run> getAllRecordedRuns() {
            	
                return List.of(
                		
                    new EasyRun(1, 5.0, Duration.ofMinutes(30), 140,LocalDate.of(2026, 10, 1), 3),
                    new EasyRun(2, 7.0, Duration.ofMinutes(42), 145,LocalDate.of(2026, 10, 1), 4),
                    new EasyRun(3, 10.0, Duration.ofMinutes(60), 150,LocalDate.of(2026, 10, 1), 5)
                    
                );
            }

            
            @Override
            public void addRun(Run run) {
            	
            }

            @Override
            public Optional<Run> getRun(int runId) {
                return Optional.empty();  
            }

            @Override
            public boolean deleteRun(int runId) {
                return false;
            }
            
        };

        RunService service = new RunService(archive);
        List<Run> results = service.searchRunByDate(searchDate);

        assertEquals(3, results.size());
    }

    @Test
    void testSearchRunByDateWithDifferentRunTypes() {

        LocalDate searchDate = LocalDate.of(2026, 10, 1);

        RunArchive archive = new RunArchive() {

            @Override
            public List<Run> getAllRecordedRuns() {
            	
                return List.of(
                		
                    new EasyRun(1, 5.0,Duration.ofMinutes(30), 140,searchDate, 3),
                    new TempoRun(2, 8.0,Duration.ofMinutes(40), 160,searchDate, 300),
                    new LongRun(3, 15.0,Duration.ofMinutes(90), 145,searchDate, 15.0),
                    new IntervalRun(4, 170,searchDate, 5,Duration.ofMinutes(3),Duration.ofMinutes(2),0.8, 0.2)
                    
                );
            }

            @Override
            public void addRun(Run run) {}

            @Override
            public Optional<Run> getRun(int runId) {
                return Optional.empty();
            }

            @Override
            public boolean deleteRun(int runId) {
                return false;
            }
        };

        RunService service = new RunService(archive);
        
        List<Run> results = service.searchRunByDate(searchDate);

        assertEquals(4, results.size());
        
        assertTrue(results.stream().anyMatch(run -> run instanceof EasyRun));
        assertTrue(results.stream().anyMatch(run -> run instanceof TempoRun));
        assertTrue(results.stream().anyMatch(run -> run instanceof LongRun));
        assertTrue(results.stream().anyMatch(run -> run instanceof IntervalRun));
        
    }


    @Test
    void testSearchRunByDatePreservesOrder() {
        LocalDate searchDate = LocalDate.of(2026, 10, 1);

        RunArchive archive = new RunArchive() {
            @Override
            public List<Run> getAllRecordedRuns() {
                return List.of(
                		
                	new EasyRun(3, 5.0, Duration.ofMinutes(30), 140,searchDate, 3),
                    new EasyRun(1, 7.0, Duration.ofMinutes(42), 145,searchDate, 4),
                    new EasyRun(2, 10.0, Duration.ofMinutes(60), 150,searchDate, 5)
                    
                );
            }

            @Override
            public void addRun(Run run) {}

            @Override
            public Optional<Run> getRun(int runId) {
                return Optional.empty();
            }

            @Override
            public boolean deleteRun(int runId) {	
                return false;
            }
        };

        RunService service = new RunService(archive);
        List<Run> results = service.searchRunByDate(searchDate);

        assertEquals(3, results.size());
        
        assertEquals(3, results.get(0).getRunId());
        assertEquals(1, results.get(1).getRunId());
        assertEquals(2, results.get(2).getRunId());
    }
    

    @Test
    void testSaveRun() {

        List<Run> runs = new ArrayList<>();

        RunArchive archive = new RunArchive() {

            @Override
            public void addRun(Run run) {
                runs.add(run);
            }

            @Override
            public List<Run> getAllRecordedRuns() {
                return runs;
            }

            @Override
            public Optional<Run> getRun(int runId) {
                return runs.stream().filter(run -> run.getRunId() == runId).findFirst();
            }

            @Override
            public boolean deleteRun(int runId) {
                return runs.removeIf( run -> run.getRunId() == runId	
                );
            }
            
        };

        RunService service = new RunService(archive);

        Run run = new EasyRun(1, 5.0, Duration.ofMinutes(30), 140,LocalDate.of(2026, 10, 1), 3);

        service.saveRun(run);

        assertEquals(1, runs.size());
        assertEquals(1, runs.get(0).getRunId());
        
    }


    
	
}
