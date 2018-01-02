CREATE TABLE USER (ID INT, NAME VARCHAR(50), ADDRESS VARCHAR(50));

CREATE TABLE PROJECTS ( projectNumber INT PRIMARY KEY ,
                        projectTitle  VARCHAR(50),
                        program VARCHAR(50),
                        category VARCHAR(50),
                        location VARCHAR(50) ,
                        region VARCHAR(50) ,
                        approvedDate DATE,
                        constructionStartDate DATE,
                        constructionEndDate DATE,
                        federalContribution DECIMAL,
                        totalEligibleCost DECIMAL,
                        ultimateRecipient VARCHAR(50) ,
                        forecastedConstructionStartDate DATE,
                        forecastedConstructionEndDate DATE);
